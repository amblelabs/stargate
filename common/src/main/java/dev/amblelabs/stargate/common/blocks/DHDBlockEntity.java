package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.api.ecs.event.DHDBlockEvents;
import dev.amblelabs.stargate.api.util.BlockEntityHelper;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DHDBlockEntity extends BlockEntity implements GeoBlockEntity,
        BlockEntityHelper.Placeable, BlockEntityHelper.Ticking {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DHDBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public DHDBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(StargateBlockEntities.DHD, blockPos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        TEvents.handle(new DHDBlockEvents.RegisterControllers(this, controllers));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void onPlace(BlockState blockState, ServerLevel level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        this.setChanged(); // TODO: figure out if this is even needed
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {

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
}
