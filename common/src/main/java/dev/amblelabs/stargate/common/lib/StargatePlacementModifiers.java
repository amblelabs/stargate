package dev.amblelabs.stargate.common.lib;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.common.worldgen.StargatePlacementModifier;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;

public class StargatePlacementModifiers {

    private static final XplatRegister<PlacementModifierType<?>> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Lazy<?> STARGATE = placementModifier("stargate", StargatePlacementModifier.CODEC);

    @SuppressWarnings("SameParameterValue")
    private static <T extends PlacementModifier> Lazy<T> placementModifier(String name, MapCodec<T> codec) {
        Supplier<PlacementModifierType<T>> type = REGISTER.register(name, () -> () -> codec);
        return () -> type.get().codec(); // FIXME: this is absolutely horrible
    }

    @FunctionalInterface
    public interface Lazy<P extends PlacementModifier> extends Supplier<PlacementModifierType<P>>, PlacementModifierType<P> {

        @Override
        default PlacementModifierType<P> get() {
            return this;
        }
    }
}
