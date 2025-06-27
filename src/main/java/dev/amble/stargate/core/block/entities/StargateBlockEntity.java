package dev.amble.stargate.core.block.entities;

import dev.amble.stargate.api.*;
import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.compat.DependencyChecker;
import dev.amble.stargate.core.StargateBlockEntities;
import dev.amble.stargate.core.StargateBlocks;
import dev.amble.stargate.core.StargateSounds;
import dev.amble.stargate.core.block.StargateBlock;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.drtheo.scheduler.api.client.ClientScheduler;
import dev.drtheo.scheduler.api.common.Scheduler;
import dev.drtheo.scheduler.api.common.TaskStage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StargateBlockEntity extends StargateLinkableBlockEntity implements StargateLinkable, BlockEntityTicker<StargateBlockEntity> {
	public AnimationState ANIM_STATE = new AnimationState();
	public AnimationState CHEVRON_LOCK_STATE = new AnimationState();
	public AnimationState IRIS_CLOSE_STATE = new AnimationState();
	public AnimationState IRIS_OPEN_STATE = new AnimationState();
	private static final Identifier SYNC_GATE_STATE = new Identifier(StargateMod.MOD_ID, "sync_gate_state");
	public int age;
	public boolean requiresPlacement = false;
	private boolean stopOpening = false;
	private boolean prevIrisState = false;
	private boolean irisSoundPlayed = false;


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

		Stargate gate = this.getStargate().get();
		Dialer dialer = gate.getDialer();

		player.sendMessage(Text.literal("ENERGY: " + gate.getEnergy()), true);

		if (gate.getEnergy() < 250) return ActionResult.FAIL;

		// drain power
		gate.removeEnergy(250);

		dialer.next();

		return ActionResult.SUCCESS;
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!(e instanceof LivingEntity)) return;
	}

	public void onBreak() {
		if (this.hasStargate()) {
			this.getStargate().get().dispose();
			this.getStargate().dispose();
		}
		this.ref = null;
		this.removeRing();
	}
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient()) return;

		this.onBreak();
		this.requiresPlacement = false;

		createRing(StargateBlocks.RING.getDefaultState(), (ServerWorld) world, false);

		Direction facing = world.getBlockState(this.getPos()).get(StargateBlock.FACING);
		DirectedGlobalPos globalPos = DirectedGlobalPos.create(world.getRegistryKey(), this.getPos(), DirectedGlobalPos.getGeneralizedRotation(facing));
		this.setStargate(StargateRef.createAs(this, Stargate.create(new Address(globalPos))));
	}

	protected int getRingRadius() {
		return 4;
	}

	/**
	 * Creates a ring of state around the stargate using text
	 * @param state the state to create the ring with
	 * @return the positions of the blocks created
	 */
	public Set<BlockPos> createRing(BlockState state, ServerWorld world, boolean force) {
		int radius = this.getRingRadius(); // Adjust the radius as needed
		Set<BlockPos> ringPositions = new HashSet<>();
		BlockPos center = this.getPos().up(radius);
		Direction facing = this.getCachedState().get(StargateBlock.FACING);

		String blockPositioning = this.getRingPositioning();

		List<String> list = blockPositioning.lines().toList();

		list.forEach((line) -> {
			for (int i = 0; i < line.length(); i++) {
				char character = line.charAt(i);
				int lineStuff = list.indexOf(line);
				if (character == 'X') {
					ringPositions.add(center.add(rotate(4 - i, 4 -lineStuff, facing)));
				}
			}
		});

		for (BlockPos pos : ringPositions) {
			if (!force && !world.getBlockState(pos).isReplaceable()) continue;

			world.setBlockState(pos, state);
		}

		return ringPositions;
	}

	protected String getRingPositioning() {
		return """
				_________
				_________
				___XXX___/
				__X___X__/
				_X_____X_//
				_X_____X_///
				_X_____X_////
				__X___X__
				___X_X___.
				""";
	}

	public static BlockPos rotate(int x, int y, Direction facing) {
		return switch (facing) {
			case NORTH -> new BlockPos(x, y, 0);
			case SOUTH -> new BlockPos(-x, y, 0);
			case WEST -> new BlockPos(0, y, x);
			case EAST -> new BlockPos(0, y, -x);
			default -> BlockPos.ORIGIN;
		};
	}
	/**
	 * Removes the ring of "StargateRingBlock" around the stargate
	 * @return the positions of the blocks removed
	 */
	public Set<BlockPos> removeRing() {
		return createRing(Blocks.AIR.getDefaultState(), (ServerWorld) this.getWorld(), true);
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
				Stargate gate = this.getStargate().get();
				Dialer dialer = gate.getDialer();
				// Run if there is a selected glyph and it is being added to the locked amount
				if (dialer.isCurrentGlyphBeingLocked()) {
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

		if (world.getServer() == null) return;

		if (this.requiresPlacement) {
			this.onPlaced(world, pos, state, null, ItemStack.EMPTY);
		}

		if (world.getServer().getTicks() % 5 == 0) {
			if (!this.hasStargate()) return;
			if (this.getGateState() != Stargate.GateState.OPEN) return;

			// Define the bounding box based on the ring radius and facing direction
			int radius = this.getRingRadius();
			Direction facing = world.getBlockState(pos).get(StargateBlock.FACING);
			Vec3d centre = Vec3d.ofCenter(pos);
			Box detectionBox = switch (facing) {
				case EAST -> new Box(centre.add(0.5, 0, -radius), centre.add(-0.5, radius * 2, radius));
				case WEST -> new Box(centre.add(0.5, 0, radius), centre.add(-0.5, radius * 2, -radius));
				case NORTH -> new Box(centre.add(-radius, 0, 0.5), centre.add(radius, radius * 2, -0.5));
				case SOUTH -> new Box(centre.add(radius, 0, 0.5), centre.add(-radius, radius * 2, -0.5));
				default -> new Box(pos, pos);
			};

			// Find entities inside the bounding box
			List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, detectionBox, e -> true);

			for (LivingEntity entity : entities) {
				// teleport the player to the stargate
				StargateCall existing = this.getStargate().get().getCurrentCall().orElse(null);

				BlockPos offset = BlockPos.ofFloored(entity.getPos().subtract(pos.toCenterPos()));
				if (existing != null && existing.to != this.getStargate().get()) {
					existing.to.teleportHere(entity, offset);
				}
			}
		}
	}

}
