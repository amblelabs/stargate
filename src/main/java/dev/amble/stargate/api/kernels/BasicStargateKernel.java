package dev.amble.stargate.api.kernels;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.lib.util.TeleportUtil;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.TeleportableEntity;
import dev.amble.stargate.api.network.ServerStargate;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.init.StargateAttributes;
import dev.amble.stargate.init.StargateDamages;
import dev.amble.stargate.init.StargateSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BasicStargateKernel extends AbstractStargateKernel implements StargateKernel.Impl {

    protected long energy = 0;
    protected Address address;

    public BasicStargateKernel(Identifier id, Stargate parent) {
        super(id, parent);
    }

    @Override
    public long maxEnergy() {
        return -1;
    }

    @Override
    public void onCreate(DirectedGlobalPos pos) {
        this.address = new Address(pos);
        this.state = new GateState.Closed();
    }

    @Override
    public boolean canTeleportFrom(LivingEntity entity) {
        if (!(this.state instanceof GateState.Open))
            return false;

        if (!(entity instanceof TeleportableEntity tp))
            return false;

        return !tp.stargate$updateAndGetStatus().isInGate();
    }

    @Override
    public void tryTeleportFrom(LivingEntity entity) {
        if (!(this.state instanceof GateState.Open open)
                || !(entity instanceof TeleportableEntity holder))
            return;

        BlockPos pos = open.target().address().pos().getPos();

        World world = entity.getWorld();
        DamageSource flow = StargateDamages.flow(world);

        if (open.callee() && !entity.isInvulnerableTo(flow)) {
            EntityAttribute attribute = StargateAttributes.SPACIAL_RESISTANCE;
            EntityAttributeInstance spacialResistance = entity.getAttributeInstance(attribute);

            float resistance = (float) (spacialResistance == null
                    ? attribute.getDefaultValue() : spacialResistance.getValue());

            resistance = 1 - resistance / 100;

            entity.damage(flow, entity.getMaxHealth() * resistance);

            // TODO: add energy conversion
            if (!entity.isAlive())
                return;
        }

        entity.setPortalCooldown(5 * 20); // 5 seconds

        DirectedGlobalPos targetPos = open.target().address().pos().offset(0, 1, 0);

        ServerWorld targetWorld = ServerLifecycleHooks.get().getWorld(targetPos.getDimension());
        BlockPos targetBlockPos = targetPos.getPos();

        if (targetWorld == null)
            return;

        world.playSound(null, pos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);
        targetWorld.playSound(null, targetBlockPos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);

        TeleportUtil.teleport(entity, targetWorld,
                targetBlockPos.toCenterPos(), targetPos.getRotationDegrees());

        holder.stargate$setStatus(TeleportableEntity.State.IN_GATE);
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
            if (timer > this.ticksPerKawoosh()) {
                timer = 0;

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

                return;
            }

            timer++;
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
        return 20 * 3;
    }

    public int ticksPerKawoosh() {
        return 20 * 3;
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
