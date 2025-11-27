package dev.amble.stargate.fluid;

import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Optional;

public abstract class LiquidNaquadahFluid extends FlowableFluid {

    @Override
    public Fluid getFlowing() {
        return StargateFluids.LIQUID_NAQUADAH_FLOWING;
    }

    @Override
    public Fluid getStill() {
        return StargateFluids.LIQUID_NAQUADAH_STILL;
    }

    @Override
    public Item getBucketItem() {
        return StargateItems.LIQUID_NAQUADAH;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
    }

    @Override
    public int getFlowSpeed(WorldView world) {
        return 2; // Prevents fluid from flowing
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return StargateBlocks.LIQUID_NAQUADAH_BLOCK.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == StargateFluids.LIQUID_NAQUADAH_STILL || fluid == StargateFluids.LIQUID_NAQUADAH_FLOWING;
    }

    @Override
    public int getLevelDecreasePerBlock(WorldView world) {
        return 1; // Prevents fluid from spreading
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
return false;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 10;
    }

    @Override
    public int getNextTickDelay(World world, BlockPos pos, FluidState oldState, FluidState newState) {
        int i = this.getTickRate(world);
        if (!oldState.isEmpty() && !newState.isEmpty() && !oldState.get(FALLING) && !newState.get(FALLING) && newState.getHeight(world, pos) > oldState.getHeight(world, pos) && world.getRandom().nextInt(4) != 0) {
            i *= 4;
        }
        return i;
    }

    @Override
    protected boolean isInfinite(World world) {
        return false;
    }

    @Override
    protected boolean hasRandomTicks() {
        return true;
    }

    @Override
    protected float getBlastResistance() {
        return 100.0F;
    }

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BOTTLE_FILL);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
        super.appendProperties(builder);
        builder.add(LEVEL);
    }

    public static class Still extends LiquidNaquadahFluid {
        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends LiquidNaquadahFluid {
        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }
    }
}