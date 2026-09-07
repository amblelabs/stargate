package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.advancements.*;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;

public class StargateAdvancementTriggers {

    private static final XplatRegister<CriterionTrigger<?>> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.TRIGGER_TYPES);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Supplier<PassedThroughTrigger> PASSED_THROUGH = REGISTER.register("passed_through", PassedThroughTrigger::new);
    public static final Supplier<BreakIrisTrigger> BREAK_IRIS = REGISTER.register("break_iris", BreakIrisTrigger::new);

    public static final Supplier<FlowDamageTrigger> FLOW_DAMAGE = REGISTER.register("damage/flow", FlowDamageTrigger::new);
    public static final Supplier<IrisDamageTrigger> IRIS_DAMAGE = REGISTER.register("damage/iris", IrisDamageTrigger::new);
    public static final Supplier<KawooshDamageTrigger> KAWOOSH_DAMAGE = REGISTER.register("damage/kawoosh", KawooshDamageTrigger::new);
}
