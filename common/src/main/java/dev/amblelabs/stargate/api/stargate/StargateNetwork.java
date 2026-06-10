package dev.amblelabs.stargate.api.stargate;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StargateNetwork extends SavedData implements Stargate.UpdateListener {

    private static final String NETWORK_FILE_ID = "stargate-networks";

    private final ServerLevel level;

    // TODO: address system
    private final Map<UUID, Stargate> lookup = new HashMap<>();

    public StargateNetwork(ServerLevel level) {
        this.level = level;
        this.setDirty();
    }

    public Stargate create() {
        Stargate result = new Stargate(UUID.randomUUID());
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
        // no-op for now
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        return tag;
    }

    public static StargateNetwork load(ServerLevel level, CompoundTag tag, HolderLookup.Provider registries) {
        return new StargateNetwork(level);
    }

    public static @Nullable StargateNetwork get(ServerLevel level) {
        return level.getDataStorage().get(factory(level), NETWORK_FILE_ID);
    }

    public static StargateNetwork getOrCreate(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(factory(level), NETWORK_FILE_ID);
    }

    public static Factory<StargateNetwork> factory(ServerLevel level) {
        return new Factory<>(() -> new StargateNetwork(level),
                (compoundTag, provider) -> load(level, compoundTag, provider),
                DataFixTypes.SAVED_DATA_FORCED_CHUNKS
        ); // FIXME: :P
    }
}
