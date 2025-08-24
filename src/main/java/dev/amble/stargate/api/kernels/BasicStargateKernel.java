package dev.amble.stargate.api.kernels;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.lib.util.TeleportUtil;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.TeleportableEntity;
import dev.amble.stargate.api.kernels.impl.OrlinGateKernel;
import dev.amble.stargate.api.network.ServerStargate;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.block.AbstractStargateBlock;
import dev.amble.stargate.init.StargateAttributes;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateDamages;
import dev.amble.stargate.init.StargateSounds;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class BasicStargateKernel extends AbstractStargateKernel implements StargateKernel.Impl {

    protected long energy = 0;
    protected Address address;
    protected float kawooshHeight = 0;
    protected int shouldKawooshOscillate = 1;

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

    public static final int TELEPORT_FREQUENCY = 10;
    private static final int TELEPORT_DELAY = 20;

    @Override
    public boolean canTeleportFrom(LivingEntity entity) {
        if (!(this.state instanceof GateState.Open))
            return false;

        if (!(entity instanceof TeleportableEntity tp))
            return false;

        return tp.stargate$updateAndGetTicks(TELEPORT_DELAY) == 0;
    }

    @Override
    public void tryTeleportFrom(LivingEntity entity) {
        if (!(this.state instanceof GateState.Open open)
                || !(entity instanceof TeleportableEntity holder))
            return;

        Stargate target = open.target().get();

        // this is most likely false, since we do a check every tick, but just in case...
        if (target == null)
            return;

        DirectedGlobalPos targetPos = target.address().pos();

        BlockPos pos = this.address().pos().getPos();

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

        Vec3d offset = entity.getPos().subtract(pos.toCenterPos().add(0, -0.5, 0));

        ServerWorld targetWorld = ServerLifecycleHooks.get().getWorld(targetPos.getDimension());
        BlockPos targetBlockPos = targetPos.getPos();

        if (targetWorld == null)
            return;

        double yOffset = 0;
        for (int y = 1; y <= 5; y++) {
            boolean bottomIsAir = targetWorld.getBlockState(targetBlockPos.up(y)).isAir();
            boolean belowIsAir = targetWorld.getBlockState(targetBlockPos.up(y - 1)).isAir();
            if (!bottomIsAir || !belowIsAir) {
                yOffset = y;
                break;
            }
        }

        world.playSound(null, pos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);
        targetWorld.playSound(null, entity.getBlockPos(), StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);

        // Check for blocks above the target position and adjust the Y offset accordingly

        // Retain entity velocity but reorient it towards the target stargate
        Vec3d velocity = entity.getVelocity();
        Vec3d direction = targetBlockPos.toCenterPos().subtract(pos.toCenterPos()).normalize();
        double speed = velocity.length();
        Vec3d newVelocity = direction.multiply(speed);

        if (!targetWorld.getBlockState(targetPos.getPos()).isAir() && targetWorld.getBlockState(targetPos.getPos()).get(AbstractStargateBlock.IRIS)) {
            entity.damage(targetWorld.getDamageSources().inWall(), Integer.MAX_VALUE); // Lol
        }

        TeleportUtil.teleport(entity, targetWorld,
                targetBlockPos.toCenterPos().add(offset).add(0, yOffset, 0),
                targetPos.getRotationDegrees()
        );

        //noinspection UnstableApiUsage
        entity.modifyAttached(StargateMod.HAS_PASSED_THROUGH_STARGATE, (bl) -> {
            if (bl == null || !bl) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 4,
                        2, false, false, true));
            }
            return true;
        });

        if (entity.canFreeze())
            entity.setFrozenTicks(100);

        entity.setVelocity(newVelocity);

        holder.stargate$setTicks(TELEPORT_DELAY);
    }

    private int timer;

    @Override
    public void tick() {
        if (!(this.parent instanceof ServerStargate))
            return;

        if (state instanceof GateState.Closed closed) {
            if (this instanceof OrlinGateKernel) {
                ServerWorld world = ServerLifecycleHooks.get().getWorld(World.NETHER);
                if (world != null) {

                    Stargate netherGate = ServerStargateNetwork.getInstance(world).getRandom();

                    if (netherGate != null && netherGate.address().pos().getDimension() == World.NETHER) {
                        if (ServerLifecycleHooks.get().getWorld(this.address.pos().getDimension()).getBlockState(this.address.pos().getPos().north()).getBlock() == StargateBlocks.TOASTER) {
                            closed.setAddress(netherGate.address().text());
                        }
                    }
                    if (closed.locked() > 6) {
                        closed.setHasDialButton(true);
                    }
                }

            }
            int length = closed.addressBuilder().length();

            if (length == 0 || (length <= closed.locked() && !closed.hasDialButton())) {
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

            if (timer >= this.ticksPerGlyph() || (closed.hasDialButton() && closed.locked() > 6)) {
                ServerWorld world = ServerLifecycleHooks.get().getWorld(this.address.pos().getDimension());
                timer = 0;

                if (!closed.hasDialButton()) {
                    closed.lock();
                }
                if (world != null) {
                    world.playSound(null,
                            this.address.pos().getPos(), StargateSounds.CHEVRON_LOCK,
                            SoundCategory.BLOCKS, 1.0f, 1.0f);
                }

                if (closed.locked() == Address.LENGTH && closed.hasDialButton()) {
                    state = new GateState.PreOpen(closed.addressBuilder(), true);
                    Stargate target = ServerStargateNetwork.get().get(closed.addressBuilder());
                    if (world != null) {
                        if (target != null && !target.address().text().isEmpty() && target != this.parent) {
                            world.playSound(null,
                                    this.address.pos().getPos(), StargateSounds.GATE_OPEN,
                                    SoundCategory.BLOCKS, 1.0f, 1.0f);
                        } else {
                            world.playSound(null,
                                    this.address.pos().getPos(), StargateSounds.GATE_FAIL,
                                    SoundCategory.BLOCKS, 1.0f, 1.0f);
                            this.state = new GateState.Closed();
                            this.markDirty();
                            return;
                        }
                    }
                    if (target != null) {
                        target.kernel().setState(new GateState.PreOpen("", false));
                        ServerWorld targetWorld = ServerLifecycleHooks.get().getWorld(target.address().pos().getDimension());
                        if (targetWorld != null) {
                            targetWorld.playSound(null,
                                    target.address().pos().getPos(), StargateSounds.GATE_OPEN,
                                    SoundCategory.BLOCKS, 1.0f, 1.0f);
                        }
                    }
                }

                this.parent.markDirty();
                Stargate target = ServerStargateNetwork.get().get(closed.addressBuilder());
                if(target != null) target.markDirty();
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
            // Adjust Bezier control points and t-mapping to linger longer near p1 and p2
            float t = (float) timer / ((float) this.ticksPerKawoosh() * 1.25f);

// Remap t to ease in and out, spending more time near p1 and p2
// Use a custom curve: t' = 3t^2 - 2t^3 (smoothstep), then stretch the middle
            float tPrime = (float) (3 * Math.pow(t, 2) - 2 * Math.pow(t, 3));
            if (tPrime > 1.0f) tPrime = 1.0f;
            if (tPrime < 0.0f) tPrime = 0.0f;

            float p0 = 0.0f;
            float p1 = 22.0f;
            float p2 = -12.0f;
            float p3 = 0.0f;
            this.kawooshHeight = (float) (
                    Math.pow(1 - tPrime, 3) * p0 +
                            3 * Math.pow(1 - tPrime, 2) * tPrime * p1 +
                            3 * (1 - tPrime) * Math.pow(tPrime, 2) * p2 +
                            Math.pow(tPrime, 3) * p3
            );

            if (tPrime >= 1.0f) {
                this.shouldKawooshOscillate = 4;
            }
            this.markDirty();
            if (timer > this.ticksPerKawoosh() && this.shouldKawooshOscillate == 4) {
                this.kawooshHeight = 0;
                this.shouldKawooshOscillate = 1;
                timer = 0;

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
        } else if (this.state instanceof GateState.Open open) {

            if (open.target().isEmpty()) {

                this.setState(new GateState.Closed());

                this.markDirty();
                ServerWorld world = ServerLifecycleHooks.get().getWorld(this.address.pos().getDimension());
                if (world != null) {
                    world.playSound(null, this.address.pos().getPos(),
                            StargateSounds.GATE_CLOSE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }

                timer = 0;

                return;
            }

            if (timer > this.ticksPerOpen()) {

                timer = 0;

                this.setState(new GateState.Closed());
                this.markDirty();
                ServerWorld world = ServerLifecycleHooks.get().getWorld(this.address.pos().getDimension());
                if (world != null) {
                    world.playSound(null, this.address.pos().getPos(),
                            StargateSounds.GATE_CLOSE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }

                Stargate gate = open.target().get();

                gate.kernel().setState(new GateState.Closed());
                gate.markDirty();
                if (world != null) {
                    world.playSound(null, gate.address().pos().getPos(),
                            StargateSounds.GATE_CLOSE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
        timer++;
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
        return 4 * 20;
    }

    public int ticksPerOpen() {
        return 30 * 20;
    }

    public float getKawooshHeight() {
        return this.kawooshHeight;
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
        this.kawooshHeight = nbt.getFloat("kawooshHeight");

        this.state = GateState.fromNbt(nbt.getCompound("State"), isSync);
    }

    @Override
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.put("Address", this.address.toNbt());
        nbt.putLong("energy", this.energy);
        nbt.putFloat("kawooshHeight", this.kawooshHeight);

        nbt.put("State", this.state.toNbt());
        return nbt;
    }

    @Override
    public void setState(GateState state) {
        this.state = state;
    }
}
