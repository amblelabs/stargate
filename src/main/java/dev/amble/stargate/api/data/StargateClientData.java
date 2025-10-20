package dev.amble.stargate.api.data;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.event.address.AddressListEvent;
import dev.drtheo.yaar.event.TEvents;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StargateClientData implements StargateData {

    private static StargateClientData INSTANCE;

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            StargateClientData.get().tick();
        });

        ServerPlayNetworking.registerGlobalReceiver(SYNC, (server, player, networkHandler, buf, sender) -> {
            long globalId = buf.readVarLong();
            NbtCompound nbt = buf.readNbt();

            StargateClientData data = StargateClientData.get();

            final Stargate stargate = data.getById(globalId);
            server.execute(() -> {
                if (stargate != null) {
                    stargate.updateStates(nbt, true);
                } else {
                    Stargate newStargate = Stargate.fromNbt(nbt, true);
                    TEvents.handle(new AddressListEvent(newStargate, id -> data.addId(id, newStargate)));
                }
            });
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
    public void addId(long id, Stargate stargate) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            int length = AddressProvider.length(id);

            if (length != 6 || length != 8)
                throw new IllegalStateException("Tried to use an address as an id!");
        }

        this.lookup.put(id, stargate);
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

    public static @NotNull StargateClientData get() {
        return INSTANCE;
    }
}
