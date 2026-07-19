package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateLifecycleEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import dev.amblelabs.stargate.api.util.BlockEntityHelper;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Set;
import java.util.UUID;

public class StargateBlockEntity extends BlockEntity implements GeoBlockEntity, Stargate.UpdateSubscriber,
        BlockEntityHelper.Placeable, BlockEntityHelper.Ticking {

    private static final String ID_TAG = "Ref";

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private @Nullable UUID stargateId = null;
    private @Nullable Stargate stargate = null;

    private @Nullable BlockState blockSet;

    public StargateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(StargateBlockEntities.STARGATE, blockPos, blockState);
    }

    public @Nullable Stargate stargate() {
        return this.stargate != null ? stargate : this.stargateId != null && this.level != null ?
                this.setStargate(StargateNetwork.get(this.level).get(this.stargateId), NbtDeserializer.Context.fromLevel(level)) : null;
    }

    public @Nullable Stargate setStargate(@Nullable Stargate stargate, NbtDeserializer.Context ctx) {
        if (stargate == null) {
            this.stargate = null;
            this.stargateId = null;
            return null;
        }

        stargate.onUpdate(this);
        TEvents.handle(new StargateLifecycleEvents.Instantiate(stargate, ctx));

        this.stargateId = stargate.getId();
        this.setChanged();

        return this.stargate = stargate;
    }

    @Override
    public void onPlace(BlockState blockState, ServerLevel level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        Stargate stargate = ServerStargateNetwork.get(level).create();

        PrototypeRegistryEntry entry = IXplatAbstractions.INSTANCE.getPrototypeRegistry().getAny().orElseThrow().value();
        entry.mark(stargate);

        this.setStargate(stargate, NbtDeserializer.Context.fromLevel(level));

        StargateBlockEvents.notify(events -> events.stargate$place(
                stargate, this, level, blockPos, this.getBlockState()));

        stargate.setChanged(); // forces sync
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        Stargate stargate = this.stargate();
        if (stargate != null) stargate.setChanged();

        return this.saveWithoutMetadata(provider);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        if (this.blockSet != null) nbt.put(StargateRingBlockEntity.ID_BLOCK, NbtUtils.writeBlockState(this.blockSet));

        if (this.stargateId != null)
            nbt.putUUID(ID_TAG, this.stargateId);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        this.blockSet = NbtUtils.readBlockState(provider.asGetterLookup().lookupOrThrow(Registries.BLOCK), nbt.getCompound(StargateRingBlockEntity.ID_BLOCK));

        if (nbt.hasUUID(ID_TAG))
            this.stargateId = nbt.getUUID(ID_TAG);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        Stargate stargate = this.stargate();
        if (stargate == null) return;

        StargateBlockEvents.notify(events -> events.stargate$registerControllers(stargate, this, controllers));
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

        BlockState state = this.getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    @Override
    public void onStargateUpdate(Stargate stargate, Set<ServerPlayer> receivers) {
        if (!(level instanceof ServerLevel serverLevel)) {
            StargateAPI.LOGGER.info("Tried to process a stargate server update on client", new IllegalStateException());
            return;
        }

        receivers.addAll(serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(this.getBlockPos()), false));
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        Stargate stargate = this.stargate();
        if (stargate == null) return;

        StargateBlockEvents.notify(events -> events.stargate$tick(stargate, this, level, blockPos, blockState));
    }

    public void setBlockSet(@Nullable BlockState state) {
        this.blockSet = state;
        this.setChanged();
    }

    public @Nullable BlockState getBlockSet() {
        return this.blockSet;
    }
}
