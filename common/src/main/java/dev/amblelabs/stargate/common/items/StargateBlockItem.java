package dev.amblelabs.stargate.common.items;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class StargateBlockItem extends BlockItem {

    private final ResourceLocation prototypeId;

    public StargateBlockItem(ResourceLocation prototypeId, Properties properties) {
        super(StargateBlocks.STARGATE, properties);

        this.prototypeId = prototypeId;
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

        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity) {
            Registry<PrototypeRegistryEntry> registry = IXplatAbstractions.INSTANCE.getPrototypeRegistry();
            PrototypeRegistryEntry entry = registry.get(this.prototypeId);

            if (entry == null) {
                StargateAPI.LOGGER.error("Failed to find prototype by id {}", this.prototypeId);
                entry = IXplatAbstractions.INSTANCE.getPrototypeRegistry().getAny().orElseThrow().value();
            }

            Stargate stargate = ServerStargateNetwork.get(serverLevel).create(entry);

            blockEntity.setStargate(stargate);
        }

        return result;
    }
}
