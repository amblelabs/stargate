package dev.amblelabs.stargate.api.stargate;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class StargateNetwork extends SavedData implements Stargate.UpdateListener {

    private static final String NETWORK_FILE_ID = "stargate-networks";
    private static final String GATES_TAG = "Gates";

    private final ServerLevel level;

    // TODO: address system
    private final Map<UUID, Stargate> lookup = new HashMap<>();

    public StargateNetwork(ServerLevel level) {
        this.level = level;
        this.setDirty();
    }

    public Stargate create() {
        Stargate result = new Stargate(UUID.randomUUID(), false);
        this.lookup.put(result.getId(), result);
        return this.init(result);
    }

    public @Nullable Stargate get(UUID key) {
        return lookup.get(key);
    }

    private Stargate init(Stargate stargate) {
        stargate.onUpdate(this);
        return stargate;
    }

    @Override
    public void onStargateUpdate(Stargate stargate) {
        this.setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag gates = new ListTag();
        for (Stargate stargate : this.lookup.values()) {
            CompoundTag gateTag = new CompoundTag();
            stargate.toNbt(gateTag, NbtSerializer.Context.fromLevel(level));

            gates.add(gateTag);
        }

        tag.put(GATES_TAG, gates);
        return tag;
    }

    public static @Nullable StargateNetwork get(ServerLevel level) {
        return level.getDataStorage().get(factory(level), NETWORK_FILE_ID);
    }

    public static StargateNetwork getOrCreate(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(factory(level), NETWORK_FILE_ID);
    }

    private static StargateNetwork load(ServerLevel level, CompoundTag tag, HolderLookup.Provider registries) {
        StargateNetwork network = new StargateNetwork(level);

        ListTag gates = tag.getList(GATES_TAG, Tag.TAG_COMPOUND);
        for (Tag rawGateTag : gates) {
            if (!(rawGateTag instanceof CompoundTag gateTag)) {
                StargateAPI.LOGGER.error("Failed to load a Stargate for {}: {}", level, rawGateTag);
                continue;
            }

            NbtDeserializer.Context ctx = NbtDeserializer.Context.fromLevel(level);
            Stargate stargate = Stargate.createFromNbt(gateTag, ctx);

            network.lookup.put(stargate.getId(), stargate);
            network.init(stargate);
        }

        return network;
    }

    private static BiFunction<CompoundTag, HolderLookup.Provider, StargateNetwork> load(ServerLevel level) {
        return (tag, registries) -> load(level, tag, registries);
    }

    private static Factory<StargateNetwork> factory(ServerLevel level) {
        return new Factory<>(() -> new StargateNetwork(level), StargateNetwork.load(level),
                null /// {@see net.fabricmc.fabric.mixin.object.builder.PersistentStateManagerMixin}
        ); // FIXME: :P
    }
}
