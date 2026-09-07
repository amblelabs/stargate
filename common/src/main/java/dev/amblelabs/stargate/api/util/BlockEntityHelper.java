package dev.amblelabs.stargate.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockEntityHelper {

    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTicker(BlockEntityType<A> serverType, BlockEntityType<E> clientType, Ticker<E> ticker) {
        return clientType == serverType ? (level, blockPos, blockState, blockEntity)
                                                -> ticker.tick((E) blockEntity, level, blockPos, blockState) : null;
    }

    public static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTicker(BlockEntityType<A> serverType, Supplier<BlockEntityType<E>> clientType, Ticker<E> ticker) {
        return createTicker(serverType, clientType.get(), ticker);
    }

    @FunctionalInterface
    public interface Ticker<T extends BlockEntity> {
        void tick(T t, Level level, BlockPos blockPos, BlockState blockState);
    }

    public interface Ticking {
        void tick(Level level, BlockPos blockPos, BlockState blockState);
    }
}
