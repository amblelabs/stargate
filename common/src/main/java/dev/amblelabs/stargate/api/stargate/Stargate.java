package dev.amblelabs.stargate.api.stargate;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.drtheo.ecs.state.TState;
import dev.drtheo.ecs.state.TStateContainer;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

public class Stargate extends TStateContainer.Delegate implements NbtSerializer, NbtDeserializer<Stargate> {

    public static final String ID_TAG = "Id";
    public static final String STATES_TAG = "States";

    private final UUID id;
    private final Set<UpdateListener> listeners = Collections.newSetFromMap(new WeakHashMap<>());

    public static Stargate createFromNbt(CompoundTag tag, NbtDeserializer.Context ctx) {
        return new Stargate(tag.getUUID(ID_TAG)).fromNbt(tag, ctx);
    }

    public Stargate(UUID id) {
        super(StargateEcs.States.createArrayHolder());

        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public void toNbt(CompoundTag nbt, NbtSerializer.Context context) {
        CompoundTag states = new CompoundTag();

        // FIXME: this only works once. by this i mean diffing.
        // FIXME FIXME: i have no idea what i was talking about. by this i mean all this.
        this.forEachState((i, state) -> stateToNbt(states, i, state, context));

        nbt.putUUID(ID_TAG, id);
        nbt.put(STATES_TAG, states);
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
        boolean fix = false;

        CompoundTag states = nbt.getCompound(STATES_TAG);

        for (String key : states.getAllKeys()) {
            if (StargateEcs.States.get(ResourceLocation.parse(key)) instanceof NbtState.Type<?> serializable) {
                Tag state = states.get(key);

                if (state instanceof CompoundTag compound) {
                    if (fix) {
                        try {
                            compound = serializable.update(compound, 0);
                        } catch (Exception e) {
                            StargateAPI.LOGGER.error("Failed to update {}", serializable, e);
                        }
                    }

                    try {
                        this.addState(serializable.decode(compound, context));
                    } catch (Exception e) {
                        StargateAPI.LOGGER.error("Failed to decode {}", serializable, e);
                    }
                } else {
                    this.removeState(serializable);
                }
            }
        }

        return this;
    }

    @Override
    public boolean addState(TState<?> state) {
        boolean result = super.addState(state);
        if (result) this.setChanged();

        return result;
    }

    @Override
    public @Nullable <T extends TState<T>> T removeState(TState.Type<T> type) {
        T result = super.removeState(type);
        if (result != null) this.setChanged();

        return result;
    }

    public void setChanged() {
        for (UpdateListener listener : this.listeners) {
            listener.onStargateUpdate(this);
        }
    }

    public void onUpdate(UpdateListener listener) {
        this.listeners.add(listener);
    }

    // eventify?
    public interface UpdateListener {
        void onStargateUpdate(Stargate stargate);
    }
}
