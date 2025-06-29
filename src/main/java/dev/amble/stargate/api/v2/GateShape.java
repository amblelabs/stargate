package dev.amble.stargate.api.v2;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface GateShape {
    void place(BlockState state, World world, BlockPos corePos, Direction direction);
    void destroy(World world, BlockPos corePos, Direction direction);
    boolean verify(BlockState state, BlockView view, BlockPos corePos, Direction direction);

    static GateShape generated(String shape) {
        return new GeneratedShape(shape).prepare();
    }

    GateShape DEFAULT = generated("""
				_________
				_________
				___XXX___/
				__X___X__/
				_X_____X_//
				_X_____X_///
				_X_____X_////
				__X___X__
				___X_X___.
				""");

    class Const implements GateShape {

        private final Iterable<BlockPos> built;

        public Const(Iterable<BlockPos> built) {
            this.built = built;
        }

        @Override
        public void place(BlockState state, World world, BlockPos corePos, Direction direction) {
            for (BlockPos pair : this.built) {
                BlockPos pos = rotate(pair, corePos, direction);
                world.setBlockState(pos, state);
            }
        }

        @Override
        public void destroy(World world, BlockPos corePos, Direction direction) {
            for (BlockPos pair : this.built) {
                BlockPos pos = rotate(pair, corePos, direction);
                world.removeBlock(pos, false);
            }
        }

        @Override
        public boolean verify(BlockState state, BlockView view, BlockPos corePos, Direction direction) {
            for (BlockPos pair : this.built) {
                BlockPos pos = rotate(pair, corePos, direction);
                BlockState placed = view.getBlockState(pos);

                if (placed.getBlock() != state.getBlock())
                    return false;
            }

            return true;
        }

        private static BlockPos rotate(BlockPos pos, BlockPos offset, Direction facing) {
            pos = rotate(pos.getX(), pos.getY(), facing);
            return pos.add(offset);
        }

        public static BlockPos rotate(int x, int y, Direction facing) {
            return switch (facing) {
                case NORTH -> new BlockPos(x, y, 0);
                case SOUTH -> new BlockPos(-x, y, 0);
                case WEST -> new BlockPos(0, y, x);
                case EAST -> new BlockPos(0, y, -x);
                default -> BlockPos.ORIGIN;
            };
        }
    }

    abstract class Built {

        protected abstract Iterable<BlockPos> build();

        protected GateShape prepare() {
            return new Const(this.build());
        }
    }

    class RingShape extends Built {
        @Override
        protected Iterable<BlockPos> build() {
            return null;
        }
        // TODO: this entire thing
    }

    class GeneratedShape extends Built {

        private final String shape;

        public GeneratedShape(String shape) {
            this.shape = shape;
        }

        // FIXME: this is obviously wrong, but it will do for now.
        @Override
        protected Iterable<BlockPos> build() {
            Set<BlockPos> ringPositions = new HashSet<>();
            List<String> list = shape.lines().toList();

            for (int j = 0; j < list.size(); j++) {
                String line = list.get(j);

                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) != 'X') continue;

                    ringPositions.add(new BlockPos(i, j, 0));
                }
            }

            return ringPositions;
        }
    }
}
