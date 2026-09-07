package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import net.minecraft.nbt.CompoundTag;

public record ChevronState(int chevrons) implements NbtState<ChevronState> {

    public static final Type<ChevronState> state = new Type<>(StargateAPI.modLoc("chevrons"), 0) {
        @Override
        public ChevronState fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            return new ChevronState(nbt.getInt("chevrons"));
        }
    };

    @Override
    public void toNbt(CompoundTag nbt, Context context) {
        nbt.putInt("chevrons", chevrons);
    }

    @Override
    public Type<ChevronState> type() {
        return state;
    }
}
