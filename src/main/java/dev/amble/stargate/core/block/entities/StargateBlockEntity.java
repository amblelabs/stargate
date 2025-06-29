package dev.amble.stargate.core.block.entities;

import dev.amble.lib.util.TeleportUtil;
import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.ServerStargate;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.api.network.StargateLinkable;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v2.MilkyWayGateKernel;
import dev.amble.stargate.compat.DependencyChecker;
import dev.amble.stargate.core.StargateBlockEntities;
import dev.amble.stargate.core.StargateBlocks;
import dev.amble.stargate.core.StargateSounds;
import dev.amble.stargate.core.block.StargateBlock;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.drtheo.scheduler.api.client.ClientScheduler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!(e instanceof LivingEntity living)) return;

		if (!this.hasStargate()) return;

		Stargate gate = this.gate().get();

		if (!(gate.state() instanceof GateState.Open open))
			return;

		TeleportUtil.teleport(living, open.target().address().pos());
	}

	public void onBreak() {
		if (this.hasStargate()) {
			Direction facing = world.getBlockState(pos).get(StargateBlock.FACING);
			this.gate().get().shape().destroy(world, pos, facing);
		}

		this.ref = null;
	}

	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient()) return;
		this.requiresPlacement = false;

		Direction facing = world.getBlockState(pos).get(StargateBlock.FACING);
		DirectedGlobalPos globalPos = DirectedGlobalPos.create(world.getRegistryKey(), this.getPos(), DirectedGlobalPos.getGeneralizedRotation(facing));

		ServerStargate stargate = new ServerStargate(globalPos, MilkyWayGateKernel::new);
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

		if (this.requiresPlacement) {
			this.onPlaced(world, pos, state, null, ItemStack.EMPTY);
		}
	}
}
