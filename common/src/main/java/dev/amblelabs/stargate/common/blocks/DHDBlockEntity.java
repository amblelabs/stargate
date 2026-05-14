package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.ecs.event.DHDBlockEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.event.TEvents;
import dev.drtheo.ecs.state.TStateContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DHDBlockEntity extends BlockEntity implements GeoBlockEntity {

    public final TStateContainer container = StargateEcs.States.createArrayHolder();

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

    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        // FIXME: this wont work properly in multiplayer, client code must handle the PrototypeIdentityState and compensate.
        PrototypeRegistryEntry entry = IXplatAbstractions.INSTANCE.getPrototypeRegistry().getAny().get().value();

        entry.make(StargateEcs.States, this.container, level.isClientSide());
        this.setChanged(); // TODO: figure out if this is even needed
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        CompoundTag states = new CompoundTag();

        // FIXME: this only works once. by this i mean diffing.
        // FIXME FIXME: i have no idea what i was talking about. by this i mean all this.
        this.container.forEachState((i, state) -> stateToNbt(states, i, state, this.level.isClientSide()));

        nbt.put(STATES_TAG, states);
    }
}
