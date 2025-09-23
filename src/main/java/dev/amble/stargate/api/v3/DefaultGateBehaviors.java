package dev.amble.stargate.api.v3;

public class DefaultGateBehaviors extends BasicGateBehaviors<DefaultGateStates> {

    public static final DefaultGateBehaviors INSTANCE = new DefaultGateBehaviors();

    @Override
    protected void init(BehaviorPopulator<DefaultGateStates> populator) {
        populator.put(GateState.StargateType.CLOSED, new ClosedBehavior());
        populator.put(GateState.StargateType.PRE_OPEN, new PreOpenBehavior());
        populator.put(GateState.StargateType.OPEN, new OpenBehavior());
    }

    static class ClosedBehavior implements GateBehavior<DefaultGateStates, DefaultGateStates.Closed> {

        @Override
        public void tick(AbstractGateKernel<DefaultGateStates> kernel, DefaultGateStates.Closed state) {

        }
    }

    static class PreOpenBehavior implements GateBehavior<DefaultGateStates, DefaultGateStates.Closed> {

        @Override
        public void tick(AbstractGateKernel<DefaultGateStates> kernel, DefaultGateStates.Closed state) {

        }
    }

    static class OpenBehavior implements GateBehavior<DefaultGateStates, DefaultGateStates.Open> {

        @Override
        public void tick(AbstractGateKernel<DefaultGateStates> kernel, DefaultGateStates.Open state) {

        }
    }
}
