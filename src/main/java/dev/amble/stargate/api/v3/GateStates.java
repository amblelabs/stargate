package dev.amble.stargate.api.v3;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface GateStates<Self extends GateStates<Self>> {

    default GateState<Self> fromNbt(NbtCompound nbt, boolean isClient) {
        GateState.Type type = GateState.Type.fromName(nbt.getString("type"));

        if (type != null) return fromNbt(type, nbt, isClient);

        StargateMod.LOGGER.error("Failed to get stargate state! Defaulting to closed.");
        return this.getDefault();

    }

    GateState<Self> getDefault();

    GateState<Self> fromNbt(GateState.Type type, NbtCompound nbt, boolean isClient);

    default NbtCompound toNbt(GateState<Self> state) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("type", state.type().toString());
        return state.toNbt(nbt);
    }

    abstract class TimerState<S extends GateStates<S>> implements GateState<S> {

        private int timer;

        public void tick() {
            this.timer++;
        }

        public int ticks() {
            return timer;
        }

        public void reset() {
            this.timer = 0;
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putInt("timer", timer);
            return nbt;
        }
    }

    abstract class Closed<Self extends Closed<Self, S>, S extends GateStates<S>> implements GateState<Self, S> {

        private String addressBuilder = "";

        public Closed() { }

        public boolean contains(char c) {
            return addressBuilder.indexOf(c) != -1;
        }

        public void appendGlyph(Glyph glyph) {
            addressBuilder += glyph.glyph();
        }

        public void appendGlyph(char glyph) {
            addressBuilder += Glyph.validate(glyph);
        }

        public String addressBuilder() {
            return addressBuilder;
        }

        public void setAddress(String address) {
            this.addressBuilder = address;
        }

        public void loadNbt(NbtCompound nbt) {
            this.addressBuilder = nbt.getString("address");
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putString("address", addressBuilder);

            return nbt;
        }

        public static <S extends GateStates<S>> Closed<S> fromNbt(NbtCompound nbt, boolean isClient) {
            Closed<S> closed = new Closed<>();
            closed.loadNbt(nbt);

            return closed;
        }
    }

    class PreOpen<S extends GateStates<S>> implements GateState<S> {

        public static final Type TYPE = Type.PRE_OPEN;
        private final String address;
        private final boolean caller;

        public PreOpen(String address, boolean caller) {
            this.address = address;
            this.caller = caller;
        }

        public String address() {
            return address;
        }

        public boolean caller() {
            return caller;
        }

        public boolean callee() {
            return !caller;
        }

        @Override
        public Type type() {
            return TYPE;
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putString("address", address);
            nbt.putBoolean("caller", caller);
            return nbt;
        }

        public static <S extends GateStates<S>> PreOpen<S> fromNbt(NbtCompound nbt, boolean isClient) {
            String id = nbt.getString("address");
            boolean caller = nbt.getBoolean("caller");
            return new PreOpen<>(id, caller);
        }
    }

    class Open<S extends GateStates<S>> implements GateState<S> {

        public static final Type TYPE = Type.OPEN;

        private final @NotNull StargateRef target;
        private final boolean caller;

        public Open(@NotNull StargateRef target, boolean caller) {
            this.target = target;
            this.caller = caller;
        }

        public Open(Stargate stargate, boolean caller) {
            this(new StargateRef(stargate), caller);
        }

        public @NotNull StargateRef target() {
            return target;
        }

        public boolean caller() {
            return caller;
        }

        public boolean callee() {
            return !caller;
        }

        @Override
        public Type type() {
            return TYPE;
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putUuid("address", this.target.id());
            nbt.putBoolean("caller", this.caller);
            return nbt;
        }

        public static <S extends GateStates<S>> GateState<S> fromNbt(NbtCompound nbt, boolean isClient) {
            UUID id = nbt.getUuid("address");
            boolean caller = nbt.getBoolean("caller");

            return new Open<>(StargateRef.createAs(isClient, id), caller);
        }
    }
}
