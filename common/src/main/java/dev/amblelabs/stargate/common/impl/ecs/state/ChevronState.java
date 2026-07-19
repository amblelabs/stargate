package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import net.minecraft.nbt.CompoundTag;

public class ChevronState implements NbtState<ChevronState> {

    public static final Type<ChevronState> state = new Type<>(StargateAPI.modLoc("chevrons"), 0) {
        @Override
        public ChevronState fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            return new ChevronState(nbt.getInt("chevrons"));
        }
    };

    public int chevrons;

    public ChevronState(int chevrons) {
        this.chevrons = chevrons;
    }

    @Override
    public void toNbt(CompoundTag nbt, NbtSerializer.Context context) {
        nbt.putInt("chevrons", chevrons);
    }

    @Override
    public Type<ChevronState> type() {
        return state;
    }
}
