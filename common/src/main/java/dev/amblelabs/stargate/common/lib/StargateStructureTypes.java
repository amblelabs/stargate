package dev.amblelabs.stargate.common.lib;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.common.worldgen.BuriedStargateStructure;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

public class StargateStructureTypes {

    private static final XplatRegistrar<StructureType<?>> REGISTRAR = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.STRUCTURE_TYPE);

    public static void register() {
        REGISTRAR.registerAll();
    }

    public static final Lazy<?> BURIED_STARGATE = type("buried_stargate", BuriedStargateStructure.CODEC);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Structure> Lazy<T> type(String name, MapCodec<T> codec) {
        Supplier<StructureType<T>> type = REGISTRAR.register(name, () -> () -> codec);
        return () -> type.get().codec(); // FIXME: this is absolutely horrible
    }

    @FunctionalInterface
    public interface Lazy<S extends Structure> extends Supplier<StructureType<S>>, StructureType<S> {

        @Override
        default StructureType<S> get() {
            return this;
        }
    }
}
