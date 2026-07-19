package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.drtheo.ecs.state.TState;
import net.minecraft.nbt.CompoundTag;

public class ShapeState implements NbtState<ShapeState> {

    public static final Type<ShapeState> state = new Type<>(StargateAPI.modLoc("shape"), 0) {
        @Override
        public ShapeState fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            return new ShapeState();
        }
    };

    @Override
    public void toNbt(CompoundTag nbt, Context context) {

    }

    @Override
    public TState.Type<ShapeState> type() {
        return state;
    }
}
