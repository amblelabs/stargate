package dev.amblelabs.lib.api.util;

import net.minecraft.core.AxisCycle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;

/**
 * doing mojang's work yet again
 */
public class MutableBlockPos extends BlockPos {

    public MutableBlockPos() {
        this(0, 0, 0);
    }

    public MutableBlockPos(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public MutableBlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public MutableBlockPos(double x, double y, double z) {
        this(Mth.floor(x), Mth.floor(y), Mth.floor(z));
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos above() {
        return this.relative(Direction.UP);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos above(int distance) {
        return this.relative(Direction.UP, distance);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos below() {
        return this.relative(Direction.DOWN);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos below(int distance) {
        return this.relative(Direction.DOWN, distance);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos north() {
        return this.relative(Direction.NORTH);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos north(int distance) {
        return this.relative(Direction.NORTH, distance);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos south() {
        return this.relative(Direction.SOUTH);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos south(int distance) {
        return this.relative(Direction.SOUTH, distance);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos west() {
        return this.relative(Direction.WEST);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos west(int distance) {
        return this.relative(Direction.WEST, distance);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos east() {
        return this.relative(Direction.EAST);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos east(int distance) {
        return this.relative(Direction.EAST, distance);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos offset(int dx, int dy, int dz) {
        if (dx != 0 || dy != 0 || dz != 0) {
            this.setX(this.getX() + dx);
            this.setY(this.getY() + dy);
            this.setZ(this.getZ() + dz);
        }

        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos multiply(int scalar) {
        if (scalar == 1) return this;

        if (scalar == 0) {
            this.setX(0);
            this.setY(0);
            this.setZ(0);
        } else {
            this.setX(this.getX() * scalar);
            this.setY(this.getY() * scalar);
            this.setZ(this.getZ() * scalar);
        }

        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos relative(Direction direction) {
        this.setX(this.getX() + direction.getStepX());
        this.setY(this.getY() + direction.getStepY());
        this.setZ(this.getZ() + direction.getStepZ());

        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos relative(Direction direction, int distance) {
        if (distance == 0)
            return this;

        this.setX(this.getX() + direction.getStepX() * distance);
        this.setY(this.getY() + direction.getStepY() * distance);
        this.setZ(this.getZ() + direction.getStepZ() * distance);

        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos relative(Direction.Axis axis, int amount) {
        if (amount != 0) {
            switch (axis) {
                case X -> this.setX(this.getX() + amount);
                case Y -> this.setY(this.getY() + amount);
                case Z -> this.setZ(this.getZ() + amount);
            }
        }

        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos rotate(Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_90 -> {
                int x = -this.getZ();
                int z = this.getX();

                this.setX(x);
                this.setZ(z);
            }
            case CLOCKWISE_180 -> {
                this.setX(-this.getX());
                this.setZ(-this.getZ());
            }
            case COUNTERCLOCKWISE_90 -> {
                int x = this.getZ();
                int z = -this.getX();

                this.setX(x);
                this.setZ(z);
            }
        }

        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos set(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos set(double x, double y, double z) {
        return this.set(Mth.floor(x), Mth.floor(y), Mth.floor(z));
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos set(Vec3i vector) {
        return this.set(vector.getX(), vector.getY(), vector.getZ());
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos set(long packedPos) {
        return this.set(getX(packedPos), getY(packedPos), getZ(packedPos));
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos set(AxisCycle cycle, int x, int y, int z) {
        return this.set(cycle.cycle(x, y, z, Direction.Axis.X), cycle.cycle(x, y, z, Direction.Axis.Y), cycle.cycle(x, y, z, Direction.Axis.Z));
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos setWithOffset(Vec3i pos, Direction direction) {
        return this.set(pos.getX() + direction.getStepX(), pos.getY() + direction.getStepY(), pos.getZ() + direction.getStepZ());
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos setWithOffset(Vec3i vector, int offsetX, int offsetY, int offsetZ) {
        return this.set(vector.getX() + offsetX, vector.getY() + offsetY, vector.getZ() + offsetZ);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos setWithOffset(Vec3i pos, Vec3i offset) {
        return this.set(pos.getX() + offset.getX(), pos.getY() + offset.getY(), pos.getZ() + offset.getZ());
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos move(Direction direction) {
        return this.move(direction, 1);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos move(Direction direction, int n) {
        return this.set(this.getX() + direction.getStepX() * n, this.getY() + direction.getStepY() * n, this.getZ() + direction.getStepZ() * n);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos move(int x, int y, int z) {
        return this.set(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos move(Vec3i offset) {
        return this.set(this.getX() + offset.getX(), this.getY() + offset.getY(), this.getZ() + offset.getZ());
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos clamp(Direction.Axis axis, int min, int max) {
        switch (axis) {
            case X -> {
                return this.set(Mth.clamp(this.getX(), min, max), this.getY(), this.getZ());
            }
            case Y -> {
                return this.set(this.getX(), Mth.clamp(this.getY(), min, max), this.getZ());
            }
            case Z -> {
                return this.set(this.getX(), this.getY(), Mth.clamp(this.getZ(), min, max));
            }
            default -> throw new IllegalStateException("Unable to clamp axis " + axis);
        }
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos setX(int x) {
        super.setX(x);
        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos setY(int y) {
        super.setY(y);
        return this;
    }

    public dev.amblelabs.lib.api.util.MutableBlockPos setZ(int z) {
        super.setZ(z);
        return this;
    }

    public BlockPos immutable() {
        return new BlockPos(this);
    }
}
