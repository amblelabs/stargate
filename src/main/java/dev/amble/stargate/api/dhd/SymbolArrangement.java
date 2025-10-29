package dev.amble.stargate.api.dhd;

import dev.amble.stargate.block.DHDBlock;
import dev.amble.stargate.entities.DHDControlEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

/**
 * Holds a control which will be run when interacted with, an
 * {@link Vector3f offset} from the centre of the {@link DHDBlock} and
 * a {@link EntityDimensions scale} for the entity <br>
 * <br>
 * A list of these is gotten by {@link DHDArrangement#SYMBOLS)} and
 * used in {@link DHDControlEntity} to hold its information
 *
 * @author loqor
 * @see DHDControlEntity
 */
public record SymbolArrangement(char symbol, EntityDimensions scale, Offset offset) {

    public DHDControlEntity createEntity(World world, BlockPos pos, Direction direction) {
        DHDControlEntity controlEntity = new DHDControlEntity(world, symbol, scale, pos);
        controlEntity.setPosition(this.offset.offset(pos.toCenterPos(), direction));

        world.spawnEntity(controlEntity);
        return controlEntity;
    }

    public sealed abstract static class Offset extends Vector3f {

        public Offset(float x, float y, float z) {
            super(x, y, z);
        }

        @Override
        public Offset rotateX(float angle) {
            return (Offset) super.rotateX(angle);
        }

        @Override
        public Offset rotateZ(float angle) {
            return (Offset) super.rotateZ(angle);
        }

        public static Offset changing(float x, float y, float z) {
            return new Changing(x, y, z);
        }

        public static Offset fixed(float x, float y, float z) {
            return new Fixed(x, y, z);
        }

        public abstract Vec3d offset(Vec3d pos, Direction direction);

        private static final class Changing extends Offset {

            public Changing(float x, float y, float z) {
                super(x, y, z);
            }

            @Override
            public Vec3d offset(Vec3d pos, Direction direction) {
                float x = this.x;

                if (direction == Direction.NORTH || direction == Direction.SOUTH)
                    x = -x;

                return pos.add(
                        x * direction.getOffsetZ() - z * direction.getOffsetX(), y,
                        x * direction.getOffsetX() - z * direction.getOffsetZ()
                );
            }
        }

        private static final class Fixed extends Offset {

            public Fixed(float x, float y, float z) {
                super(x, y, z);
            }

            @Override
            public Vec3d offset(Vec3d pos, Direction direction) {
                return pos.add(x, y, z);
            }
        }
    }
}
