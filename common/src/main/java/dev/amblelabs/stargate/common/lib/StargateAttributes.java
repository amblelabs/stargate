package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class StargateAttributes {

    private static final XplatRegistrar<Attribute> REGISTRAR = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.ATTRIBUTE);

    public static void register() {
        REGISTRAR.registerAll();
    }

    public static final Holder<Attribute> SPACIAL_RESISTANCE = ranged("spacial_resistance", 0, 0, 100,
            attribute -> attribute.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE));

    @SuppressWarnings("SameParameterValue")
    private static Holder<Attribute> ranged(String name, double fallback, double min, double max, UnaryOperator<Attribute> op) {
        return make(name, () -> op.apply(new RangedAttribute("attribute." + StargateAPI.MOD_ID + "." + name, fallback, min, max)));
    }

    private static <T extends Attribute> Holder<Attribute> make(String id, Supplier<T> attr) {
        return REGISTRAR.registerHolder(id, attr);
    }
}
