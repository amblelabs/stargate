package dev.amble.stargate.api;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.gates.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

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
        return Registry.register(REGISTRY, id, new Entry() {
            @Override
            public Stargate load(NbtCompound nbt, boolean isClient) {
                return loader.load(nbt, isClient);
            }

            @Override
            public Stargate create(ServerWorld world, BlockPos pos, Direction direction) {
                return creator.create(world, pos, direction);
            }
        });
    }

    public interface GateCreator {
        Stargate create(ServerWorld world, BlockPos pos, Direction direction);
    }

    public interface GateLoader {
        Stargate load(NbtCompound nbt, boolean isClient);
    }

    public abstract static class Entry implements GateCreator, GateLoader { }
}
