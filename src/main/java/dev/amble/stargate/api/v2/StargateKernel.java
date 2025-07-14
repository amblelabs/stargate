package dev.amble.stargate.api.v2;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.NbtSync;
import dev.amble.stargate.api.StargateAccessor;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.init.StargateSounds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public interface StargateKernel extends NbtSync {

    default void onCreate(DirectedGlobalPos pos) {}

    default void tick() { }

    Address address();

    long energy();
    long maxEnergy();

    long energyToDial(Address address);

    default boolean hasEnoughEnergy(Address address) {
        return this.maxEnergy() == -1 || this.energy() >= this.energyToDial(address);
    }

    boolean canDialTo(Stargate stargate);

    GateShape shape();
    GateState state();

    boolean dirty();
    void markDirty();
    void unmarkDirty();

    abstract class Impl implements StargateKernel {

        private final Identifier id;
        protected final Stargate parent;

        protected GateState state;
        protected boolean dirty;

        public Impl(Identifier id, Stargate parent) {
            this.id = id;
            this.parent = parent;
        }

        public Identifier id() {
            return id;
        }

        @Override
        public GateState state() {
            return state;
        }

        @Override
        public boolean dirty() {
            return dirty;
        }

        @Override
        public void unmarkDirty() {
            this.dirty = false;
        }

        @Override
        public void markDirty() {
            this.dirty = true;
        }
    }

    abstract class Basic extends Impl implements StargateAccessor {

        protected long energy = 0;
        protected Address address;

        public Basic(Identifier id, Stargate parent) {
            super(id, parent);
        }

        @Override
        public void onCreate(DirectedGlobalPos pos) {
            this.address = new Address(pos);
            this.state = new GateState.Closed();
        }

        @Override
        public long maxEnergy() {
            return -1;
        }

        private static final int ticksPerGlyph = 2 * 30;

        private int timer;

        @Override
        public void tick() {
            if (!(this.parent instanceof ServerStargate))
                return;

            if (state instanceof GateState.Closed closed) {
                int length = closed.addressBuilder().length();

                if (length == 0 || length <= closed.locked()) {
                    if (closed.locking()) {
                        closed.setLocking(false);
                        this.parent.markDirty();
                    }

                    if (length == 0 && closed.locked() > 0) {
                        closed.setLocked(0);
                        this.parent.markDirty();
                    }

                    return;
                }

                if (timer >= ticksPerGlyph) {
                    ServerWorld world = ServerLifecycleHooks.get().getWorld(this.address.pos().getDimension());
                    timer = 0;

                    closed.lock();
                    if (world != null) {
                        world.playSound(null,
                                this.address.pos().getPos(), StargateSounds.CHEVRON_LOCK,
                                SoundCategory.BLOCKS, 1.0f, 1.0f);
                    }

                    if (closed.locked() == Address.LENGTH) {
                        if (world != null) {
                            world.playSound(null,
                                    this.address.pos().getPos(), StargateSounds.GATE_OPEN,
                                    SoundCategory.BLOCKS, 1.0f, 1.0f);
                        }
                        state = new GateState.PreOpen(closed.addressBuilder());
                    }

                    this.parent.markDirty();
                    return;
                }

                if (!closed.locking()) {
                    closed.setLocking(true);
                    this.parent.markDirty();
                }

                timer++;
            } else if (state instanceof GateState.PreOpen preOpen) {
                // Handle missing gates by address gracefully
                Stargate target = ServerStargateNetwork.get().get(preOpen.address());

                if (target == null || !this.canDialTo(target) || !this.hasEnoughEnergy(target.address())) {
                    state = new GateState.Closed();
                } else {
                    // TODO: open the portal on the target side too
                    state = new GateState.Open(target);
                    ((StargateAccessor) target.kernel()).setState(state);
                }

                this.parent.markDirty();
                if (target != null)
                    target.markDirty();
            }
        }

        @Override
        public Address address() {
            return address;
        }

        @Override
        public long energy() {
            return energy;
        }

        @Override
        public GateShape shape() {
            return GateShape.DEFAULT;
        }

        @Override
        public boolean canDialTo(Stargate stargate) {
            return !(stargate.state() instanceof GateState.Open);
        }

        @Override
        public void loadNbt(NbtCompound nbt, boolean isSync) {
            this.address = Address.fromNbt(nbt.getCompound("Address"));
            this.energy = nbt.getInt("energy");

            this.state = GateState.fromNbt(nbt.getCompound("State"));
        }

        @Override
        public NbtCompound toNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.put("Address", this.address.toNbt());
            nbt.putLong("energy", this.energy);

            nbt.put("State", this.state.toNbt());
            return nbt;
        }

        @Override
        public void setState(GateState state) {
            this.state = state;
        }
    }
}
