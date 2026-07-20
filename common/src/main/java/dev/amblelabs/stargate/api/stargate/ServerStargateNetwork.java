package dev.amblelabs.stargate.api.stargate;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.ecs.event.StargateLifecycleEvents;
import dev.amblelabs.stargate.common.impl.ecs.state.C7State;
import dev.amblelabs.stargate.common.network.StargateSyncS2CPayload;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ServerStargateNetwork extends StargateNetwork {

    public static final Map<UUID, Stargate> GLOBAL = new HashMap<>();
    public static final Map<String, Stargate> C7 = new HashMap<>();

    private static final String NETWORK_FILE_ID = "stargate-networks";
    private static final String GATES_TAG = "Gates";

    private final Persistent persistent;
    private final ServerLevel level;

    public ServerStargateNetwork(ServerLevel level) {
        this.level = level;
        this.persistent = level.getDataStorage().computeIfAbsent(factory(), NETWORK_FILE_ID);
    }

    @Override
    public void tick(Stargate stargate) {
        super.tick(stargate);

        if (!stargate.isChanged()) return;

        Set<ServerPlayer> players = stargate.collectUpdateReceivers();
        if (players == null) return;

        this.send(stargate, players);
    }

    private void send(Stargate stargate, Set<ServerPlayer> players) {
        CompoundTag tag = new CompoundTag();
        stargate.toNbt(tag, NbtSerializer.Context.fromLevel(level));

        IXplatAbstractions.INSTANCE.sendPacketToAll(players.stream(), new StargateSyncS2CPayload(tag));
    }

    public Stargate create(PrototypeRegistryEntry entry) {
        Stargate result = new Stargate(UUID.randomUUID(), false);
        entry.mark(result);

        this.lookup.put(result.getId(), result);
        GLOBAL.put(result.getId(), result);

        StargateLifecycleEvents.notify(events
                -> events.stargate$instantiate(result, NbtDeserializer.Context.fromLevel(level)));

        C7.put(result.state(C7State.state).address(), result);
        return result;
    }

    @Override
    public void remove(UUID id) {
        Stargate stargate = this.get(id);

        super.remove(id);
        GLOBAL.remove(id);
        C7.remove(stargate.state(C7State.state).address());
    }

    class Persistent extends SavedData {

        public Persistent() {
            this.setDirty();
        }

        public void load(CompoundTag tag) {
            ListTag gates = tag.getList(GATES_TAG, Tag.TAG_COMPOUND);

            for (Tag rawGateTag : gates) {
                if (!(rawGateTag instanceof CompoundTag gateTag)) {
                    StargateAPI.LOGGER.error("Failed to load a Stargate for {}: {}", level, rawGateTag);
                    continue;
                }

                NbtDeserializer.Context ctx = NbtDeserializer.Context.fromLevel(level);
                Stargate stargate = Stargate.createFromNbt(gateTag, ctx);

                ServerStargateNetwork.this.lookup.put(stargate.getId(), stargate);
                GLOBAL.put(stargate.getId(), stargate);

                StargateLifecycleEvents.notify(events
                        -> events.stargate$instantiate(stargate, ctx));

                C7.put(stargate.state(C7State.state).address(), stargate);
            }
        }

        @Override
        public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
            ListTag gates = new ListTag();
            for (Stargate stargate : ServerStargateNetwork.this.lookup.values()) {
                CompoundTag gateTag = new CompoundTag();
                stargate.toNbt(gateTag, NbtSerializer.Context.fromLevel(level));

                gates.add(gateTag);
            }

            tag.put(GATES_TAG, gates);
            return tag;
        }
    }

    private Persistent load(CompoundTag tag, HolderLookup.Provider registries) {
        Persistent network = new Persistent();
        network.load(tag);

        return network;
    }

    private SavedData.Factory<Persistent> factory() {
        return new SavedData.Factory<>(Persistent::new, this::load,
                null /// {@see net.fabricmc.fabric.mixin.object.builder.PersistentStateManagerMixin}
        );
    }

    public static ServerStargateNetwork get(ServerLevel level) {
        return (ServerStargateNetwork) StargateNetwork.get(level);
    }
}
