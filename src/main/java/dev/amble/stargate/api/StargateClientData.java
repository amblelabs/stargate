package dev.amble.stargate.api;

import dev.amble.stargate.api.v3.Stargate;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.jetbrains.annotations.Nullable;

public class StargateClientData implements StargateData {

    private static StargateClientData INSTANCE;

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            StargateClientData data = StargateClientData.get();

            if (data != null)
                data.tick();
        });

        // should it be re-initialized on join/leave?
        INSTANCE = new StargateClientData();
    }

    private final Long2ObjectMap<Stargate> lookup = new Long2ObjectOpenHashMap<>();

    private void tick() {
        for (Stargate stargate : this.lookup.values()) {
            stargate.tick();
        }
    }

    @Override
    public void removeId(long id) {
        lookup.remove(id);
    }

    @Override
    public boolean containsId(long id) {
        return lookup.containsKey(id);
    }

    @Override
    public @Nullable Stargate getById(long id) {
        return lookup.get(id);
    }

    public static StargateClientData get() {
        return INSTANCE;
    }
}
