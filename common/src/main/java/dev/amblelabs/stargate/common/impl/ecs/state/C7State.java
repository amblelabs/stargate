package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.address.Glyph;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.drtheo.ecs.state.TState;
import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public record C7State(String address) implements NbtState<C7State> {

    public static final Type<C7State> state = new Type<>(StargateAPI.modLoc("c7"), 0) {
        @Override
        public C7State fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            String address = nbt.getString("address");

            if (address.isEmpty()) {
                for (int i = 0; i < 7; i++) {
                    int idx = new Random().nextInt(Glyph.ALPHABET_LENGTH);
                    address += Glyph.idxToChar(idx);
                }
            }

            return new C7State(address);
        }
    };

    @Override
    public void toNbt(CompoundTag nbt, Context context) {
        nbt.putString("address", address);
    }

    @Override
    public TState.Type<C7State> type() {
        return state;
    }
}
