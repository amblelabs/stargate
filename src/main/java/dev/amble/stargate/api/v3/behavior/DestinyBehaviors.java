package dev.amble.stargate.api.v3.behavior;

import dev.amble.stargate.api.address.Address;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.yaar.behavior.Resolve;
import net.minecraft.util.math.MathHelper;

public interface DestinyBehaviors {

    class Closed extends BasicGateBehaviors.Closed implements StargateEvents {

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            BasicGateStates.Closed closed = stargate.state(BasicGateStates.Closed.state);

            int length = closed.address.length();
            closed.locking = length > closed.locked;

            // fixup broken state
            if (length < closed.locked) {
                closed.locked = length;
                closed.timer = 0;

                stargate.markDirty();
                return;
            }

            if (closed.locking && closed.timer++ >= BasicGateStates.Closed.TICKS_PER_CHEVRON) {
                closed.timer = 0;
                closed.locked++;

                stargate.playSound(StargateSounds.CHEVRON_LOCK);
                stargate.markDirty();
                return;
            }

            if (closed.locked == Address.LENGTH) {
                Stargate target = ServerStargateNetwork.get().get(closed.address);

                if (target == null || target == stargate) {
                    stargate.playSound(StargateSounds.GATE_FAIL);

                    manager.set(stargate, new BasicGateStates.Closed());
                    return;
                }

                manager.set(stargate, new BasicGateStates.Opening(closed.address, true));
                manager.set(target, new BasicGateStates.Opening("", false));

                target.markDirty();
            }
        }
    }

    class Opening extends BasicGateBehaviors.Opening implements StargateEvents {

        static final float p0 = 0f;
        static final float p1 = 22f;
        static final float p2 = -12f;
        static final float p3 = 0;

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            BasicGateStates.Opening opening = stargate.state(BasicGateStates.Opening.state);

            // Adjust Bezier control points and t-mapping to linger longer near p1 and p2
            float t = (float) opening.timer / ((float) BasicGateStates.Opening.TICKS_PER_KAWOOSH * 1.25f);

            // Remap t to ease in and out, spending more time near p1 and p2
            // Use a custom curve: t' = 3t^2 - 2t^3 (smoothstep), then stretch the middle
            float tPrime = MathHelper.clamp((float) (3 * Math.pow(t, 2) - 2 * Math.pow(t, 3)), 0, 1);

            opening.kawooshHeight = (float) (
                    Math.pow(1 - tPrime, 3) * p0 +
                            3 * Math.pow(1 - tPrime, 2) * tPrime * p1 +
                            3 * (1 - tPrime) * Math.pow(tPrime, 2) * p2 +
                            Math.pow(tPrime, 3) * p3
            );

            stargate.markDirty();

            if (opening.timer++ > BasicGateStates.Opening.TICKS_PER_KAWOOSH && tPrime == 1) {
                opening.kawooshHeight = 0;
                opening.timer = 0;

                // Handle missing gates by address gracefully
                Stargate target = ServerStargateNetwork.get().get(opening.address);

                if (opening.caller && target != null) { // TODO: add distance/protocol compat checks here
                    manager.set(stargate, new BasicGateStates.Open(new StargateRef(target), true));
                    manager.set(target, new BasicGateStates.Open(new StargateRef(stargate), false));
                } else {
                    manager.set(stargate, new BasicGateStates.Closed());
                }
            }
        }
    }

    class Open extends BasicGateBehaviors.Open implements StargateEvents {

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;
            
            BasicGateStates.Open open = stargate.state(BasicGateStates.Open.state);

            // handle abnormal state
            if (open.target.isEmpty()) {
                manager.set(stargate, new BasicGateStates.Closed());
                return;
            }

            if (open.timer++ > BasicGateStates.Open.TICKS_PER_OPEN) {
                open.timer = 0;

                manager.set(stargate, new BasicGateStates.Closed());
                manager.set(open.target.get(), new BasicGateStates.Closed());
            }
        }
    }
}
