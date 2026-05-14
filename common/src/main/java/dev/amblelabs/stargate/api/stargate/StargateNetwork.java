package dev.amblelabs.stargate.api.stargate;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class StargateNetwork extends SavedData {

    private static final String NETWORK_FILE_ID = "stargate-networks";

    private final ServerLevel level;

    public StargateNetwork(ServerLevel level) {
        this.level = level;
        this.setDirty();
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
        return new Factory<>(() -> new StargateNetwork(level), (compoundTag, provider) -> load(level, compoundTag, provider), DataFixTypes.SAVED_DATA_COMMAND_STORAGE);
    }
}
