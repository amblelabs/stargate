package dev.amblelabs.stargate.client.api.stargate;

import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ClientStargateNetwork extends StargateNetwork {

    private final ClientLevel level;

    public ClientStargateNetwork(ClientLevel level) {
        this.level = level;
    }

    public void add(Stargate tardis) {
        this.lookup.put(tardis.getId(), tardis);
    }

    public void upsert(CompoundTag tag) {
        UUID id = tag.getUUID(Stargate.ID_TAG); // FIXME kinda sucks
        Stargate tardis = this.get(id);

        NbtDeserializer.Context ctx = NbtDeserializer.Context.fromLevel(level);

        if (tardis != null) {
            tardis.fromNbt(tag, ctx);
        } else {
            tardis = Stargate.createFromNbt(tag, ctx);
            this.add(tardis);

//            TardisLifecycleEvents.handleLoaded(this, tardis);
        }
    }

    public void remove(UUID id) {
        this.lookup.remove(id);
    }

    public boolean contains(UUID id) {
        return lookup.containsKey(id);
    }

    public @Nullable Stargate get(UUID id) {
        return lookup.get(id);
    }

    private void clear() {
        this.lookup.clear();
    }

    public static ClientStargateNetwork get(ClientLevel level) {
        return (ClientStargateNetwork) StargateNetwork.get(level);
    }
}
