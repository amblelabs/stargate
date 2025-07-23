package dev.amble.stargate.api.v2.kernels.base;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.*;
import dev.amble.stargate.init.StargateSounds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public abstract class BasicStargateKernel extends AbstractStargateKernel implements StargateKernel.Impl {

    protected long energy = 0;
    protected Address address;

    public BasicStargateKernel(Identifier id, Stargate parent) {
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

            if (timer >= this.ticksPerGlyph()) {
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
        } else if (this.state instanceof GateState.PreOpen preOpen) {
            // Handle missing gates by address gracefully
            Stargate target = ServerStargateNetwork.get().get(preOpen.address());

            if (target == null || !this.canDialTo(target) || !this.hasEnoughEnergy(target.address())) {
                this.state = new GateState.Closed();
            } else {
                this.state = new GateState.Open(target, true);
                target.kernel().setState(new GateState.Open(this.parent, false));
            }

            this.markDirty();

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

    public int ticksPerGlyph() {
        return 2 * 30;
    }

    @Override
    public GateShape shape() {
        return GateShape.DEFAULT;
    }

    /**
     * @implNote Checks if the target gate is not open.
     * Consider calling the super method on override.
     */
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
