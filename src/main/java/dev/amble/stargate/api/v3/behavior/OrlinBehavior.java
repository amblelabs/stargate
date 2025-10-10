package dev.amble.stargate.api.v3.behavior;

import dev.amble.stargate.api.v3.OrlinGate;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public interface OrlinBehavior {

    class Open extends BasicGateBehaviors.Open {

        // TODO: move to component
        public static final Box NS_BOX = new Box(BlockPos.ORIGIN).expand(0, 0, 0).expand(0, 1, 0);
        public static final Box WE_BOX = new Box(BlockPos.ORIGIN).expand(0, 0, 0).expand(0, 1, 0);

        public Open() {
            super(NS_BOX, WE_BOX);
        }

        @Override
        public void block$tick(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state) {
            if (!(stargate instanceof OrlinGate)) return;
            super.block$tick(stargate, entity, world, pos, state);
        }
    }
}
