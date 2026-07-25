package dev.amblelabs.stargate.api.stargate;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.common.impl.ecs.state.PrototypeIdentityState;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.drtheo.ecs.state.TState;
import dev.drtheo.ecs.state.TStateContainer;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Stargate extends TStateContainer.Delegate implements NbtSerializer, NbtDeserializer<Stargate> {

    public static final int DEFAULT_VERSION = 0;

    public static final String TAG_ID = "Id";
    public static final String TAG_STATES = "States";

    private final UUID id;
    private final boolean isClient;
    private final Set<UpdateSubscriber> subscribers = Collections.newSetFromMap(new WeakHashMap<>());

    public static Stargate createFromNbt(CompoundTag tag, NbtDeserializer.Context ctx) {
        return new Stargate(tag.getUUID(TAG_ID), ctx.isClient()).fromNbt(tag, ctx);
    }

    public Stargate(UUID id, boolean isClient) {
        super(StargateEcs.States.createArrayHolder());

        this.id = id;
        this.isClient = isClient;
    }

    public UUID getId() {
        return id;
    }

    public boolean isClient() {
        return isClient;
    }

    @Override
    public void toNbt(CompoundTag nbt, NbtSerializer.Context context) {
        CompoundTag states = new CompoundTag();

        // TODO: diff updates
        this.forEachState((i, state) -> stateToNbt(states, i, state, context));

        nbt.putUUID(TAG_ID, id);
        nbt.put(TAG_STATES, states);
    }

    @SuppressWarnings("rawtypes")
    private <T extends TState<T>> void stateToNbt(CompoundTag nbt, int i, @Nullable TState<T> state, NbtSerializer.Context context) {
        if (state == null) {
            // do the diffing only if we're serializing for client
            if (!context.isClient()) return;

            TState.Type<?> type = StargateEcs.States.get(i);
            if (type == null) return;

            nbt.put(type.id().toString(), ByteTag.ZERO);
            return;
        }

        TState.Type<T> type = state.type();

        if (!(type instanceof NbtState.Type backed))
            return;

        //noinspection unchecked
        CompoundTag tag = backed.encode(state, context);
        if (tag == null) return;

        nbt.put(type.id().toString(), tag);
    }

    @Override
    @Contract(mutates = "this")
    public Stargate fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
        CompoundTag states = nbt.getCompound(TAG_STATES);

        if (this.isClient()) {
            this.forEachState((index, state) -> {
                if (state != null && state.type() instanceof NbtState.Type<?> serializable) {
                    CompoundTag stateTag = null;
                    String key = serializable.id().toString();

                    if (states.contains(key, Tag.TAG_COMPOUND)) {
                        stateTag = states.getCompound(key);
                        states.remove(key);
                    }

                    this.fromNbt(serializable, stateTag, context);
                }
            });
        }

        for (String key : states.getAllKeys()) {
            if (StargateEcs.States.get(ResourceLocation.parse(key)) instanceof NbtState.Type<?> serializable) {
                this.fromNbt(serializable, states.getCompound(key), context);
            }
        }

        return this;
    }

    private void fromNbt(NbtState.Type<?> type, @Nullable CompoundTag tag, NbtDeserializer.Context context) {
        if (tag == null) {
            this.removeState(type);
            return;
        }

        if (context.type() == NbtDeserializer.Context.Type.LOAD) {
            try {
                tag = type.update(tag, DEFAULT_VERSION);
            } catch (Exception e) {
                StargateAPI.LOGGER.error("Failed to update {}", type, e);
            }
        }

        try {
            this.addState(type.decode(tag, context));
        } catch (Exception e) {
            StargateAPI.LOGGER.error("Failed to decode {}", type, e);
        }
    }

    public TStateContainer getStatic() {
        PrototypeIdentityState prototype = this.stateOrNull(PrototypeIdentityState.state);
        if (prototype == null) return this;

        return prototype.prototype().staticStates().orElse(this);
    }

    @Override
    public boolean addState(TState<?> state) {
        if (state instanceof NbtState<?> nbtState)
            this.updateState(nbtState);

        boolean result = super.addState(state);
        if (result) this.setChanged();

        return result;
    }

    private <T extends NbtState<T>> void updateState(NbtState<T> state) {
        if (!this.hasState(state.type())) return;

        T prev = this.stateOrNull(state.type());
        if (prev == null || prev.getClass() != state.getClass()) return;

        state.migrate(prev);
    }

    @Override
    public @Nullable <T extends TState<? extends T>> T removeState(TState.Type<? extends T> type) {
        T result = super.removeState(type);
        if (result != null) this.setChanged();

        return result;
    }

    private boolean dirty = false;

    public void setChanged() {
        this.dirty = true;
    }

    public boolean isChanged() {
        return dirty;
    }

    /**
     * Removes dirty state and collects update receivers.
     * @return set of player recipients or {@code null} if the state is not dirty
     */
    public @Nullable Set<ServerPlayer> collectUpdateReceivers() {
        if (!this.dirty) return null;

        Set<ServerPlayer> set = new HashSet<>();
        for (UpdateSubscriber subscriber : this.subscribers) {
            subscriber.onStargateUpdate(this, set);
        }

        this.dirty = false;
        return set;
    }

    public void onUpdate(UpdateSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    @FunctionalInterface
    public interface UpdateSubscriber {
        void onStargateUpdate(Stargate stargate, Set<ServerPlayer> receivers);
    }
}
