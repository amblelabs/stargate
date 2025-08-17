package dev.amble.stargate.api.kernels;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public sealed interface GateState {

    String type();

    NbtCompound toNbt(NbtCompound nbt);

    default NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("type", this.type());
        return toNbt(nbt);
    }

    static GateState fromNbt(NbtCompound nbt, boolean isClient) {
        return switch (nbt.getString("type")) {
            case Closed.TYPE -> Closed.fromNbt(nbt);
            case PreOpen.TYPE -> PreOpen.fromNbt(nbt);
            case Open.TYPE -> Open.fromNbt(isClient, nbt);
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
        private boolean hasDialButton;

        private String addressBuilder = "";

        public Closed() {
            this(0, false);
        }

        public Closed(int locked) {
            this.locked = locked;
        }

        public Closed(boolean hasDialButton) {
            this(0, hasDialButton);
        }

        public Closed(int locked, boolean hasDialButton) {
            this.locked = locked;
            this.hasDialButton = hasDialButton;
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

        public void setHasDialButton(boolean hasDialButton) {
            this.hasDialButton = hasDialButton;
        }

        public boolean hasDialButton() {
            return hasDialButton;
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
            this.hasDialButton = nbt.getBoolean("hasDialButton");
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putInt("locked", locked);
            nbt.putBoolean("locking", locking);
            nbt.putString("address", addressBuilder);
            nbt.putBoolean("hasDialButton", hasDialButton);

            return nbt;
        }

        static Closed fromNbt(NbtCompound nbt) {
            Closed closed = new Closed();
            closed.loadNbt(nbt);

            return closed;
        }
    }

    record PreOpen(String address, boolean caller) implements GateState {

        static final String TYPE = "PreOpen";

        public String address() {
            return address;
        }

        public boolean callee() {
            return !caller;
        }

        @Override
        public String type() {
            return TYPE;
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putString("address", address);
            nbt.putBoolean("caller", caller);
            return nbt;
        }

        static PreOpen fromNbt(NbtCompound nbt) {
            String id = nbt.getString("address");
            boolean caller = nbt.getBoolean("caller");
            return new PreOpen(id, caller);
        }
    }

    record Open(@NotNull StargateRef target, boolean caller) implements GateState {

        static final String TYPE = "Open";

        public Open(Stargate stargate, boolean caller) {
            this(new StargateRef(stargate), caller);
        }

        public boolean callee() {
            return !caller;
        }

        @Override
        public String type() {
            return TYPE;
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            nbt.putUuid("address", this.target.id());
            nbt.putBoolean("caller", this.caller);
            return nbt;
        }

        static GateState fromNbt(boolean isClient, NbtCompound nbt) {
            UUID id = nbt.getUuid("address");
            boolean caller = nbt.getBoolean("caller");

            return new Open(StargateRef.createAs(isClient, id), caller);
        }
    }
}
