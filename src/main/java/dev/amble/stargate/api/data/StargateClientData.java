package dev.amble.stargate.api.data;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.address.AddressListEvent;
import dev.drtheo.yaar.event.TEvents;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StargateClientData implements StargateData {

    private static StargateClientData INSTANCE;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(world -> StargateClientData.get().tick());

        ClientPlayNetworking.registerGlobalReceiver(SYNC, (client, networkHandler, buf, sender) -> {
            long globalId = buf.readVarLong();
            NbtCompound nbt = buf.readNbt();

            System.out.println("Received sg " + globalId);

            StargateClientData data = StargateClientData.get();
            final Stargate stargate = data.getById(globalId);

            client.execute(() -> {
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
        System.out.println("> getting sg by id " + id);
        return lookup.get(id);
    }

    public static @NotNull StargateClientData get() {
        return INSTANCE;
    }
}
