package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.blocks.StargateRingBlock;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ShapeBehavior implements TBehavior, StargateBlockEvents {

    private static final String SHAPE = """
				_________
				_________
				___XXX___/
				__X___X__/
				_X_____X_//
				_X_____X_///
				_X_____X_////
				__X___X__
				___X_X___.
				""";

    @SuppressWarnings("DataFlowIssue")
    public static ShapeBehavior INSTANCE = null;

    public ShapeBehavior() {
        INSTANCE = this;
    }

    private static Stream<BlockPos> forEachPos(Direction direction, BlockPos origin) {
        List<String> lines = SHAPE.lines().toList();

        int height = lines.size();
        int width = lines.stream().mapToInt(String::length).max().orElse(0);
        int xOffset = width / 2;
        int yOffset = height / 2;

        return IntStream.range(0, height)
                .boxed()
                .flatMap(j -> {
                    String line = lines.get(j);
                    return IntStream.range(0, line.length())
                            .filter(i -> line.charAt(i) == 'X')
                            .mapToObj(i -> rotate(
                                    new BlockPos(i - xOffset + 2, yOffset - j + 4, 0),
                                    origin,
                                    direction
                            ));
                });
    }

//    @Override
    public boolean stargate$prePlace(Direction direction, ServerLevelAccessor level, BlockPos pos) {
        return forEachPos(direction, pos).map(level::getBlockState)
                .allMatch(BlockBehaviour.BlockStateBase::canBeReplaced);
    }

    @Override
    public void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevelAccessor level, BlockPos pos) {
        Direction direction = state.getValue(StargateBlock.FACING);

        forEachPos(direction, pos).forEach(ringPos -> {
            FluidState fluidState = level.getFluidState(ringPos);

            // TODO: use a proper BlockState resolver
            level.setBlock(ringPos, StargateBlocks.RING.defaultBlockState()
                    .setValue(StargateRingBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER), Block.UPDATE_ALL);
        });
    }

    @Override
    public void stargate$break(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        Direction direction = state.getValue(StargateBlock.FACING);

        forEachPos(direction, pos).forEach(ringPos -> level.removeBlock(ringPos, false));
    }

    private static BlockPos rotate(BlockPos pos, BlockPos offset, Direction facing) {
        pos = rotate(pos.getX(), pos.getY(), facing);
        return pos.offset(offset);
    }

    public static BlockPos rotate(int x, int y, Direction facing) {
        return switch (facing) {
            case NORTH -> new BlockPos(x, y, 0);
            case SOUTH -> new BlockPos(-x, y, 0);
            case WEST -> new BlockPos(0, y, x);
            case EAST -> new BlockPos(0, y, -x);
            default -> BlockPos.ZERO;
        };
    }

    @Override
    public void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) { }

    @Override
    public void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) { }

    @Override
    public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) { }

    @Override
    public void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { }

    @Override
    public void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) { }
}
