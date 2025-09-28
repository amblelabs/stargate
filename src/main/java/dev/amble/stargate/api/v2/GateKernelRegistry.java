package dev.amble.stargate.api.v2;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v3.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class GateKernelRegistry {

    private static final RegistryKey<Registry<Entry>> KEY = RegistryKey.ofRegistry(StargateMod.id("kernel"));
    private static final DefaultedRegistry<Entry> REGISTRY = FabricRegistryBuilder
            .createDefaulted(KEY, MilkyWayGate.ID).buildAndRegister();

    public static DefaultedRegistry<Entry> get() {
        return REGISTRY;
    }

    public static Entry MILKY_WAY = register(MilkyWayGate.ID, MilkyWayGate::new, MilkyWayGate::new);
    public static Entry ORLIN = register(OrlinGate.ID, OrlinGate::new, OrlinGate::new);
    public static Entry PEGASUS = register(PegasusGate.ID, PegasusGate::new, PegasusGate::new);
    public static Entry DESTINY = register(DestinyGate.ID, DestinyGate::new, DestinyGate::new);

    public static Entry register(String path, GateCreator creator, GateLoader loader) {
        return register(StargateMod.id(path), creator, loader);
    }

    public static Entry register(Identifier id, GateCreator creator, GateLoader loader) {
        return Registry.register(REGISTRY, id, new Entry(creator, loader));
    }

    public interface GateCreator {
        Stargate create(DirectedGlobalPos pos);
    }

    public interface GateLoader {
        Stargate load(NbtCompound nbt, boolean isClient);
    }

    public record Entry(GateCreator creator, GateLoader loader) {

    }
}
