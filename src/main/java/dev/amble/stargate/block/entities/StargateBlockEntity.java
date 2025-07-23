package dev.amble.stargate.block.entities;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.lib.util.TeleportUtil;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.network.StargateLinkable;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.ServerStargate;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.compat.DependencyChecker;
import dev.amble.stargate.init.*;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.drtheo.scheduler.api.client.ClientScheduler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StargateBlockEntity extends StargateLinkableBlockEntity implements StargateLinkable, BlockEntityTicker<StargateBlockEntity> {
	public AnimationState ANIM_STATE = new AnimationState();
	public AnimationState CHEVRON_LOCK_STATE = new AnimationState();
	public AnimationState IRIS_CLOSE_STATE = new AnimationState();
	public AnimationState IRIS_OPEN_STATE = new AnimationState();
    public int age;
	public boolean requiresPlacement = false;
	private boolean stopOpening = false;
	private boolean prevIrisState = false;

	public StargateBlockEntity(BlockPos pos, BlockState state) {
		super(StargateBlockEntities.STARGATE, pos, state);
	}

	@Nullable @Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (!this.hasStargate()) return ActionResult.FAIL;
		if (world.isClient()) return ActionResult.SUCCESS;
		if (!DependencyChecker.hasTechEnergy()) return ActionResult.FAIL; // require power mod

		Stargate gate = this.gate().get();

		// FIXME: replace literals
		player.sendMessage(Text.literal("ENERGY: " + gate.energy()), true);

		if (gate.energy() < 250) return ActionResult.FAIL;

		// drain power
		// TODO: figure this out
		// gate.removeEnergy(250);

		// TODO whatever the fuck thats supposed to do
		// dialer.next();

		return ActionResult.SUCCESS;
	}

	public void onEnterHitbox(BlockPos pos, Entity e, Box box) {
		if (!(e instanceof LivingEntity living) || living.hasPortalCooldown())
			return;

		if (!this.hasStargate()) return;

		Stargate gate = this.gate().get();

		// TODO: everything after this comment should be moved for StargateKernel to handle.
		if (!(gate.state() instanceof GateState.Open open))
			return;

		World world = this.getWorld();

		if (world == null || !living.getBoundingBox().intersects(box))
			return;

		// shatter
		DamageSource flow = StargateDamages.flow(world);

		if (open.callee() && !living.isInvulnerableTo(flow)) {
			EntityAttributeInstance spacialResistance = living.getAttributeInstance(StargateAttributes.SPACIAL_RESISTANCE);

			float resistance = spacialResistance == null ? 0 : (float) spacialResistance.getValue();
			resistance = 1 - resistance / 100;

			living.damage(flow, living.getMaxHealth() * resistance);
		}

		living.setPortalCooldown(5 * 20); // 5 seconds

		DirectedGlobalPos targetPos = open.target().address().pos().offset(0, 1, 0);
		ServerWorld targetWorld = world.getServer().getWorld(targetPos.getDimension());
		BlockPos targetBlockPos = targetPos.getPos();

		if (targetWorld == null)
			return;

		world.playSound(null, pos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);
		targetWorld.playSound(null, targetBlockPos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);

		TeleportUtil.teleport(living, targetWorld,
				targetBlockPos.toCenterPos(), targetPos.getRotationDegrees());
	}

	public void onBreak() {
		if (this.hasStargate()) {
			Direction facing = world.getBlockState(pos).get(StargateBlock.FACING);
			this.gate().get().shape().destroy(world, pos, facing);
		}

		this.ref = null;
	}

	public void onPlacedWithKernel(World world, BlockPos pos, GateKernelRegistry.KernelCreator kernelCreator) {
		if (world.isClient()) return;
		this.requiresPlacement = false;

		Direction facing = world.getBlockState(pos).get(StargateBlock.FACING);
		DirectedGlobalPos globalPos = DirectedGlobalPos.create(world.getRegistryKey(), this.getPos(), DirectedGlobalPos.getGeneralizedRotation(facing));

		ServerStargate stargate = new ServerStargate(globalPos, kernelCreator);
		ServerStargateNetwork.get().add(stargate);

		this.setStargate(StargateRef.createAs(this, stargate));

		stargate.shape().place(StargateBlocks.RING.getDefaultState(), world, this.pos, facing);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, StargateBlockEntity blockEntity) {
		boolean irisState = this.getCachedState().get(StargateBlock.IRIS);

		if (!world.isClient()) {
			// Play sound only when IRIS state changes
			if (irisState != prevIrisState) {
				if (irisState) {
					world.playSound(null, this.getPos(), StargateSounds.IRIS_CLOSE, SoundCategory.BLOCKS, 1.0f, 1.0f);
				} else {
					world.playSound(null, this.getPos(), StargateSounds.IRIS_OPEN, SoundCategory.BLOCKS, 1.0f, 1.0f);
				}
				prevIrisState = irisState;
			}

			Direction facing = world.getBlockState(pos).get(StargateBlock.FACING);
			Box northSouthBox = new Box(this.getPos()).expand(2, 2, 0).offset(0, 3, 0);
			Box westEastBox = new Box(this.getPos()).expand(0, 2, 2).offset(0, 3, 0);;
			Box box = switch (facing) {
				case WEST, EAST  -> westEastBox;
				default -> northSouthBox;
			};

			if (ServerLifecycleHooks.get().getTicks() % 20 == 0) {
				List<Entity> entities = world.getOtherEntities(null, box, e -> e != null && e.isAlive() && !e.isSpectator());

				for (Entity e : entities) {
					if (e instanceof LivingEntity living) {
						onEnterHitbox(pos, living, box);
					}
				}
			}
		}

		if (world.isClient()) {
			age++;

			ANIM_STATE.startIfNotRunning(age);

			if (this.hasStargate()) {
				Stargate gate = this.gate().get();
				// Run if there is a selected glyph and it is being added to the locked amount
				if (gate.state() instanceof GateState.Closed closed && closed.locking()) {
					CHEVRON_LOCK_STATE.startIfNotRunning(age);
					IRIS_CLOSE_STATE.stop();
					IRIS_OPEN_STATE.stop();
				} else {
					CHEVRON_LOCK_STATE.stop();
				}

				if (irisState) {
					IRIS_CLOSE_STATE.startIfNotRunning(age);
					CHEVRON_LOCK_STATE.stop();
					IRIS_OPEN_STATE.stop();
					stopOpening = false;
				} else {
					if (!stopOpening) {
						CHEVRON_LOCK_STATE.stop();
						IRIS_CLOSE_STATE.stop();
						IRIS_OPEN_STATE.startIfNotRunning(age);
					}
					if (IRIS_OPEN_STATE.isRunning()) {
						ClientScheduler.get().runTaskLater(() -> {
							IRIS_OPEN_STATE.stop();
							stopOpening = true;
						}, TimeUnit.SECONDS, 3);
					}
				}
			}
		}
	}
}
