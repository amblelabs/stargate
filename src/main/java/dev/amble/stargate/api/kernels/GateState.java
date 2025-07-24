package dev.amble.stargate.api.kernels;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public sealed interface GateState {

    String type();

    NbtCompound toNbt(NbtCompound nbt);

    default NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("type", this.type());
        return toNbt(nbt);
    }

    static GateState fromNbt(NbtCompound nbt) {
        return switch (nbt.getString("type")) {
            case Closed.TYPE -> Closed.fromNbt(nbt);
            case PreOpen.TYPE -> PreOpen.fromNbt(nbt);
            case Open.TYPE -> Open.fromNbt(nbt);
            default -> {
                StargateMod.LOGGER.error("Failed to get stargate state! Defaulting to closed.");
                yield new Closed();
            }
        };
    }

    final class Closed implements GateState {

        static final String TYPE = "Closed";

        private int locked;
        private boolean locking;

        private String addressBuilder = "";

        public Closed() {
            this(0);
        }

        public Closed(int locked) {
            this.locked = locked;
        }

        public int locked() {
            return locked;
        }

        public void setLocked(int locked) {
            this.locked = locked;
        }

        public void lock() {
            this.locked++;
        }

        public boolean locking() {
            return locking;
        }

        public void setLocking(boolean locking) {
            this.locking = locking;
        }

        public boolean isDialing() {
            return locking || locked > 0;
        }

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

        @Override
        public String type() {
            return TYPE;
        }

        public void loadNbt(NbtCompound nbt) {
            this.locked = nbt.getInt("locked");
            this.locking = nbt.getBoolean("locking");
            this.addressBuilder = nbt.getString("address");
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putInt("locked", locked);
            nbt.putBoolean("locking", locking);
            nbt.putString("address", addressBuilder);

            return nbt;
        }

        static Closed fromNbt(NbtCompound nbt) {
            Closed closed = new Closed();
            closed.loadNbt(nbt);

            return closed;
        }
    }

    record PreOpen(String address) implements GateState {

        static final String TYPE = "PreOpen";

        public String address() {
            return address;
        }

        @Override
        public String type() {
            return TYPE;
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putString("address", address);
            return nbt;
        }

        static PreOpen fromNbt(NbtCompound nbt) {
            return new PreOpen(nbt.getString("address"));
        }
    }

    record Open(@NotNull Stargate target, boolean caller) implements GateState {

        static final String TYPE = "Open";

        public boolean callee() {
            return !caller;
        }

        @Override
        public String type() {
            return TYPE;
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.put("address", this.target.address().toNbt());
            nbt.putBoolean("caller", this.caller);
            return nbt;
        }

        static GateState fromNbt(NbtCompound nbt) {
            Address address = Address.fromNbt(nbt.getCompound("address"));
            boolean caller = nbt.getBoolean("caller");

            Stargate stargate = ServerStargateNetwork.get().get(address);

            if (stargate == null)
                return new Closed();

            return new Open(stargate, caller);
        }
    }
}
