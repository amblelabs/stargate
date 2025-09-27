package dev.amble.stargate.api.v3.behavior;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public interface OrlinBehavior {

    class Open extends BasicGateBehaviors.Open {

        public static final Box NS_BOX = new Box(BlockPos.ORIGIN).expand(0, 0, 0).expand(0, 1, 0);
        public static final Box WE_BOX = new Box(BlockPos.ORIGIN).expand(0, 0, 0).expand(0, 1, 0);

        public Open() {
            super(NS_BOX, WE_BOX);
        }
    }
}
