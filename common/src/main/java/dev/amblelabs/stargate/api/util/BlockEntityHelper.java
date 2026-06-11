package dev.amblelabs.stargate.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityHelper {

    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTicker(BlockEntityType<A> serverType, BlockEntityType<E> clientType, Ticker<E> ticker) {
        return clientType == serverType ? (level, blockPos, blockState, blockEntity)
                                          -> ticker.tick((E) blockEntity, level, blockPos, blockState) : null;
    }

    @FunctionalInterface
    public interface Ticker<T extends BlockEntity> {
        void tick(T t, Level level, BlockPos blockPos, BlockState blockState);
    }

    public interface Ticking {
        void tick(Level level, BlockPos blockPos, BlockState blockState);
    }

    public interface Placeable {
        void onPlace(BlockState blockState, ServerLevel level, BlockPos blockPos, BlockState blockState2, boolean bl);
    }
}
