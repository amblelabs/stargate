package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;

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

    @Override
    public void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, ServerLevel level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(StargateBlock.FACING);

        List<String> list = SHAPE.lines().toList();

        int height = list.size();
        int width = list.stream().mapToInt(String::length).max().orElse(0);
        int xOffset = width / 2;
        int yOffset = height / 2;

        for (int j = 0; j < height; j++) {
            String line = list.get(j);

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) != 'X') continue;

                BlockPos pos = rotate(new BlockPos(i - xOffset + 2, yOffset - j + 4, 0), blockPos, direction);
                level.setBlock(pos, StargateBlocks.RING.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
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
    public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Player player, BlockHitResult blockHitResult) { }

    @Override
    public void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) { }
}
