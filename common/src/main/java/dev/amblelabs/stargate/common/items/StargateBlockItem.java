package dev.amblelabs.stargate.common.items;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.Prototype;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.mod.StargateTags;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.behavior.ShapeBehavior;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class StargateBlockItem extends BlockItem {

    private final @Nullable ResourceLocation prototypeId;

    private @Nullable String descriptionId;

    public StargateBlockItem(@Nullable ResourceLocation prototypeId, Properties properties) {
        super(StargateBlocks.STARGATE, properties);

        this.prototypeId = prototypeId;
    }

    public StargateBlockItem(Properties properties) {
        this(null, properties);
    }

    @Override
    public String getDescriptionId() {
        if (this.descriptionId == null)
            this.descriptionId = Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this));

        return this.descriptionId;
    }

    // this is needed, because who knows when this item will be registered...
    // the block reference may be null at that point.
    @Override
    public Block getBlock() {
        return StargateBlocks.STARGATE;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        Level level = context.getLevel();

        if (!result.indicateItemUse())
            return result;

        BlockPos pos = context.getClickedPos();

        if (!(level instanceof ServerLevel serverLevel) || !(level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity))
            return result;

        Direction direction = blockEntity.getBlockState().getValue(StargateBlock.FACING);
        boolean success = ShapeBehavior.INSTANCE.stargate$prePlace(direction, serverLevel, pos);

        if (!success) {
            serverLevel.removeBlock(pos, false);
            return InteractionResult.FAIL;
        }

        Prototype prototype = findPrototype(this.prototypeId, level.random);
        Stargate stargate = ServerStargateNetwork.get(serverLevel).create(prototype);

        blockEntity.setStargate(stargate);

        StargateBlockEvents.notify(events -> events.stargate$place(
                stargate, blockEntity, level.getBlockState(pos), serverLevel, pos));

        stargate.setChanged(); // forces sync
        return result;
    }

    public static Prototype findPrototype(@Nullable ResourceLocation prototypeId, RandomSource random) {
        Registry<Prototype> registry = XplatAbstractions.INSTANCE.getPrototypeRegistry();

        if (prototypeId == null)
            return pickRandomPrototype(random).value();

        Prototype entry = registry.get(prototypeId);

        if (entry == null) {
            StargateAPI.LOGGER.error("Failed to find prototype by id {}", prototypeId);
            Holder<Prototype> holder = pickRandomPrototype(random);

            StargateAPI.LOGGER.warn("...falling back to {}", holder.unwrapKey().map(ResourceKey::location));
            entry = holder.value();
        }

        return entry;
    }

    public static Holder<Prototype> pickRandomPrototype(RandomSource random) {
        return XplatAbstractions.INSTANCE.getPrototypeRegistry()
                .getRandomElementOf(StargateTags.Prototypes.PLACEABLE, random)
                .orElseThrow();
    }
}
