package dev.amblelabs.stargate.client.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.api.util.NbtUtil;
import dev.drtheo.ecs.state.TState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class GlyphsState implements NbtState<GlyphsState> {

    public static final Type<GlyphsState> state = new Type<>(StargateAPI.modLoc("glyphs"), 0) {
        @Override
        public GlyphsState fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            return new GlyphsState(NbtUtil.getInt(nbt, "amount", 36),
                    NbtUtil.getFloat(nbt, "radius", 142),
                    NbtUtil.getLoc(nbt, "font")
            );
        }
    };

    public final int amount;
    public final float radius;
    public final @Nullable ResourceLocation font;

    public GlyphsState(int amount, float radius, @Nullable ResourceLocation font) {
        this.amount = amount;
        this.radius = radius;
        this.font = font;
    }

    @Override
    public void toNbt(CompoundTag nbt, Context context) {

    }

    @Override
    public TState.Type<GlyphsState> type() {
        return state;
    }
}
