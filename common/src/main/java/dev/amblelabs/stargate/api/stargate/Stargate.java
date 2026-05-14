package dev.amblelabs.stargate.api.stargate;

import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.drtheo.ecs.state.NbtDeserializer;
import dev.drtheo.ecs.state.NbtSerializer;
import dev.drtheo.ecs.state.TState;
import dev.drtheo.ecs.state.TStateContainer;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class Stargate extends TStateContainer.Delegate implements NbtSerializer, NbtDeserializer<Stargate> {

    public static final String STATES_TAG = "States";

    public Stargate() {
        super(StargateEcs.States.createArrayHolder());
    }

    @Override
    public void toNbt(CompoundTag nbt, boolean isClient) {
        CompoundTag states = new CompoundTag();

        // FIXME: this only works once. by this i mean diffing.
        // FIXME FIXME: i have no idea what i was talking about. by this i mean all this.
        this.forEachState((i, state) -> stateToNbt(states, i, state, isClient));

        nbt.put(STATES_TAG, states);
    }

    @SuppressWarnings("rawtypes")
    private <T extends TState<T>> void stateToNbt(CompoundTag nbt, int i, @Nullable TState<T> state, boolean isClient) {
        if (state == null) {
            // do the diffing only if we're serializing for client
            if (isClient) nbt.put(StargateEcs.States.get(i).id().toString(), ByteTag.ZERO);

            return;
        }

        TState.Type<T> type = state.type();

        if (!(type instanceof TState.NbtBacked backed))
            return;

        //noinspection unchecked
        CompoundTag tag = backed.encode(state, isClient);
        if (tag == null) return;

        nbt.put(type.id().toString(), tag);
    }

    @Override
    @Contract(mutates = "this")
    public Stargate fromNbt(CompoundTag nbt, boolean isClient) {
        boolean fix = false;

        CompoundTag states = nbt.getCompound(STATES_TAG);

        for (String key : states.getAllKeys()) {
            if (StargateEcs.States.get(ResourceLocation.parse(key)) instanceof TState.NbtBacked<?> serializable) {
                Tag state = states.get(key);

                if (state instanceof CompoundTag compound) {
                    this.addState(serializable.decode(fix ? serializable.update(compound, 0) : compound, isClient));
                } else {
                    this.removeState(serializable);
                }
            }
        }

        return this;
    }
}
