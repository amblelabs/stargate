package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.amblelabs.stargate.common.lib.StargateDamageTypes;
import dev.drtheo.ecs.behavior.Resolve;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

public class KawooshBehavior implements TBehavior, StargateBlockEvents.Tick {

    public static final float KAWOOSH_MAX_DISTANCE = 5.5f;
    public static final float MAX_RADIUS = 2.6f;

    @Resolve
    private final GateManagerBehavior manager = behavior();

    @Override
    public void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) {
        if (level instanceof ServerLevel serverLevel && serverLevel.getServer().getTickCount() % 2 == 0
                && this.manager.getCurrent(stargate) instanceof GateState.Opening open && open.timer < GateState.Opening.TICKS_PER_KAWOOSH) {
            Direction direction = blockState.getValue(StargateBlock.FACING);
            Vector3f offset = direction.step().mul(KAWOOSH_MAX_DISTANCE / 2f);

            AABB aabb = AABB.ofSize(blockPos.getCenter(), KAWOOSH_MAX_DISTANCE, MAX_RADIUS, KAWOOSH_MAX_DISTANCE)
                    .move(offset.x, MAX_RADIUS / 2, offset.z);

            for (Entity entity : level.getEntities(null, aabb)) {
                entity.hurt(StargateDamageTypes.source(level, StargateDamageTypes.KAWOOSH), Integer.MAX_VALUE);
            }
        }
    }
}
