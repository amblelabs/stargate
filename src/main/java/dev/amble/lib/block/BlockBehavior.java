package dev.amble.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;

public interface BlockBehavior<T extends BlockBehavior<T>> {

    default void init(Block block) { }

    default BlockState initDefaultState(Block block, BlockState state) {
        return state;
    }

    default void appendProperties(StateManager.Builder<Block, BlockState> builder) { }

    Entry<T> type();

    record Entry<T extends BlockBehavior<T>>(int index) {

        public T get(ABlock block) {
            return (T) block.behaviors[index];
        }
    }
}
