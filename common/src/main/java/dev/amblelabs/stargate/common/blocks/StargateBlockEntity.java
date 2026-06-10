package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateLifecycleEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class StargateBlockEntity extends BlockEntity implements GeoBlockEntity, Stargate.UpdateListener {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public @Nullable Stargate stargate = null;

    public StargateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(StargateBlockEntities.STARGATE, blockPos, blockState);
    }

    public void onPlace(BlockState blockState, ServerLevel level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        Stargate stargate = StargateNetwork.getOrCreate(level).create();

        PrototypeRegistryEntry entry = IXplatAbstractions.INSTANCE.getPrototypeRegistry().getAny().orElseThrow().value();
        entry.mark(stargate);

        this.setStargate(stargate, NbtDeserializer.Context.fromLevel(level));
    }

    public Stargate setStargate(Stargate stargate, NbtDeserializer.Context ctx) {
        stargate.onUpdate(this);
        TEvents.handle(new StargateLifecycleEvents.Instantiate(stargate, ctx));
        return this.stargate = stargate;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return super.saveWithoutMetadata(provider);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        stargate.toNbt(nbt, NbtSerializer.Context.fromLevel(level));
        nbt.putUUID("Ref", this.stargate.getId()); // TODO: figure out if this is needed
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        NbtDeserializer.Context ctx = NbtDeserializer.Context.fromLevel(level);

        this.stargate = this.stargate != null ? this.stargate.fromNbt(nbt, ctx)
                : this.setStargate(Stargate.createFromNbt(nbt, ctx), ctx);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        TEvents.handle(new StargateBlockEvents.RegisterControllers(Objects.requireNonNull(this.stargate), this, controllers));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null || level.isClientSide()) return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    @Override
    public void onStargateUpdate(Stargate stargate) {
        this.setChanged();
    }
}
