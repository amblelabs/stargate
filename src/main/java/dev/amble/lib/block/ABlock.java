package dev.amble.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ABlock extends Block {

    final BlockBehavior<?>[] behaviors;

    public ABlock(Settings settings, BlockBehavior<?>... behaviors) {
        super(settings);

        this.behaviors = BlockBehaviors.behaviors.toArray(new BlockBehavior[0]);

        for (BlockBehavior<?> behavior : behaviors) {
            behaviors[behavior.type().index()] = behavior;
        }

        BlockState defState = this.getDefaultState();

        for (BlockBehavior<?> behavior : behaviors) {
            behavior.init(this);
            defState = behavior.initDefaultState(this, defState);
        }

        this.setDefaultState(defState);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return BlockPlacementBehavior.IDX.get(this).getPlacementState(this.getDefaultState(), ctx);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return BlockRotationBehavior.IDX.get(this).rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return BlockRotationBehavior.IDX.get(this).mirror(state, mirror);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        for (BlockBehavior<?> behavior : behaviors) {
            behavior.appendProperties(builder);
        }
    }
}
