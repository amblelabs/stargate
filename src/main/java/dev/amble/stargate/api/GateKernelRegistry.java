package dev.amble.stargate.api;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.state.stargate.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.function.Supplier;

public class GateKernelRegistry {

    private static final RegistryKey<Registry<Entry>> KEY = RegistryKey.ofRegistry(StargateMod.id("kernel"));
    private static final DefaultedRegistry<Entry> REGISTRY = FabricRegistryBuilder
            .createDefaulted(KEY, MilkyWayState.ID).buildAndRegister();

    public static DefaultedRegistry<Entry> get() {
        return REGISTRY;
    }

    public static final Entry MILKY_WAY = register(MilkyWayState.ID, MilkyWayState::new);
    public static final Entry ORLIN = register(OrlinState.ID, OrlinState::new);
    public static final Entry PEGASUS = register(PegasusState.ID, PegasusState::new);
    public static final Entry DESTINY = register(DestinyState.ID, DestinyState::new);

    public static Entry register(String path, Supplier<GateIdentityState> creator) {
        return register(StargateMod.id(path), creator);
    }

    public static Entry register(Identifier id, Supplier<GateIdentityState> creator) {
        return Registry.register(REGISTRY, id, new Entry(creator));
    }

    public record Entry(Supplier<GateIdentityState> creator) {

        public Stargate create(ServerWorld world, BlockPos pos, Direction direction) {
            return new Stargate(creator.get(), world, pos, direction);
        }

        public Stargate load(NbtCompound nbt, boolean isClient) {
            return new Stargate(creator.get(), nbt, isClient);
        }
    }
}
