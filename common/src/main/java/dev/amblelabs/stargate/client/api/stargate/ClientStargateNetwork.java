package dev.amblelabs.stargate.client.api.stargate;

import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class ClientStargateNetwork extends StargateNetwork {

    private final ClientLevel level;

    public ClientStargateNetwork(ClientLevel level) {
        this.level = level;
    }

    public void add(Stargate stargate) {
        this.lookup.put(stargate.getId(), stargate);
    }

    public void upsert(CompoundTag tag) {
        UUID id = tag.getUUID(Stargate.TAG_ID); // FIXME kinda sucks
        Stargate stargate = this.get(id);

        NbtDeserializer.Context ctx = NbtDeserializer.Context.forUpdate(level).get();

        if (stargate != null) {
            stargate.fromNbt(tag, ctx);
        } else {
            stargate = Stargate.createFromNbt(tag, ctx);
            this.add(stargate);
        }
    }

    public static ClientStargateNetwork get(ClientLevel level) {
        return (ClientStargateNetwork) StargateNetwork.get(level);
    }
}
