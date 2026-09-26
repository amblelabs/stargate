package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.advancements.*;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;

public class StargateAdvancementTriggers {

    private static final XplatRegistrar<CriterionTrigger<?>> REGISTRAR = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.TRIGGER_TYPES);

    public static void register() {
        REGISTRAR.registerAll();
    }

    public static final Supplier<PassedThroughTrigger> PASSED_THROUGH = REGISTRAR.register("passed_through", PassedThroughTrigger::new);
    public static final Supplier<BreakIrisTrigger> BREAK_IRIS = REGISTRAR.register("break_iris", BreakIrisTrigger::new);
    public static final Supplier<StargateDialTrigger> DIAL = REGISTRAR.register("dial", StargateDialTrigger::new);

    public static final Supplier<FlowDamageTrigger> FLOW_DAMAGE = REGISTRAR.register("damage/flow", FlowDamageTrigger::new);
    public static final Supplier<IrisDamageTrigger> IRIS_DAMAGE = REGISTRAR.register("damage/iris", IrisDamageTrigger::new);
    public static final Supplier<KawooshDamageTrigger> KAWOOSH_DAMAGE = REGISTRAR.register("damage/kawoosh", KawooshDamageTrigger::new);
}
