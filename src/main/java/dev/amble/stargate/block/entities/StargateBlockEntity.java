package dev.amble.stargate.block.entities;

import com.mojang.serialization.DataResult;
import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.kernels.BasicStargateKernel;
import dev.amble.stargate.api.kernels.GateState;
import dev.amble.stargate.api.kernels.StargateKernel;
import dev.amble.stargate.api.kernels.impl.OrlinGateKernel;
import dev.amble.stargate.api.network.ServerStargate;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.network.StargateLinkable;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.compat.DependencyChecker;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.drtheo.scheduler.api.client.ClientScheduler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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
	private BlockState blockSet;

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

	@Override
	public @Nullable Object getRenderData() {
		return this;
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		if (this.blockSet != null) {
			nbt.put("blockSet", NbtHelper.fromBlockState(this.blockSet));
		}
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		DataResult<BlockState> blockStateDataResult = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockSet"));
		this.setBlockSet(blockStateDataResult.result().orElse(null));
		super.readNbt(nbt);
	}

	public void setBlockSet(BlockState state) {
		this.blockSet = state;
		this.markDirty();
		this.sync();
	}

	public BlockState getBlockSet() {
		return this.blockSet;
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!this.hasStargate()) return ActionResult.FAIL;

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof StargateBlockEntity stargateBlockEntity) {
			ItemStack heldItem = player.getStackInHand(hand);
			if (!heldItem.isEmpty()) {
				if (heldItem.getItem() instanceof BlockItem blockItem) {
					if (blockItem.getBlock().getDefaultState() != stargateBlockEntity.getBlockSet()) {
						stargateBlockEntity.setBlockSet(blockItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, player.getMainHandStack(), hit)));
						stargateBlockEntity.markDirty();
						return ActionResult.SUCCESS;
					}
					return ActionResult.FAIL;
				}
			} else {
				stargateBlockEntity.setBlockSet(null);
				stargateBlockEntity.markDirty();
				return ActionResult.SUCCESS;
			}
		}

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
		if (!(e instanceof LivingEntity living))
			return;

		if (!this.hasStargate())
			return;

		Stargate gate = this.gate().get();

		if (!gate.canTeleportFrom(living))
			return;

		gate.tryTeleportFrom(living);
	}

	public void onBreak(BlockState state) {
		if (this.hasStargate()) {
			Stargate gate = this.gate().get();
			Direction facing = state.get(StargateBlock.FACING);

			gate.shape().destroy(world, pos, facing);
			gate.dispose();
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

		this.setStargate(new StargateRef(stargate));

		stargate.shape().place(StargateBlocks.RING.getDefaultState(), world, this.pos, facing);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, StargateBlockEntity blockEntity) {
		boolean irisState = this.getCachedState().get(StargateBlock.IRIS);

		if (!world.isClient()) {
			if (this.gate() == null || this.gate().isEmpty()) return;
			Stargate gate = this.gate().get();
			if (gate == null) return;

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
			Box westEastBox = new Box(this.getPos()).expand(0, 2, 2).offset(0, 3, 0);
			Box orlinNorthSouthBox = new Box(this.getPos()).expand(0, 0, 0).offset(0, 1, 0);
			Box orlinWestEastBox = new Box(this.getPos()).expand(0, 0, 0).offset(0, 1, 0);
			StargateKernel kernel = gate.kernel();
			if (!(kernel.state() instanceof GateState.Open)) return;
			boolean bl = kernel instanceof OrlinGateKernel;
			Box box = switch (facing) {
				case WEST, EAST  -> bl ? orlinWestEastBox : westEastBox;
				default -> bl ? orlinNorthSouthBox : northSouthBox;
			};

			if (ServerLifecycleHooks.get().getTicks() % BasicStargateKernel.TELEPORT_FREQUENCY == 0) {
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

			if (this.hasStargate()) {
				Stargate gate = this.gate().get();

				ANIM_STATE.startIfNotRunning(age);

				// Run if there is a selected glyph and it is being added to the locked amount
				if (gate.state() instanceof GateState.Closed closed && closed.locking()) {
					//CHEVRON_LOCK_STATE.startIfNotRunning(age);
					IRIS_CLOSE_STATE.stop();
					IRIS_OPEN_STATE.stop();
					return;
				} /*else {
					CHEVRON_LOCK_STATE.stop();
				}*/

				if (irisState) {
					IRIS_CLOSE_STATE.startIfNotRunning(age);
					//CHEVRON_LOCK_STATE.stop();
					IRIS_OPEN_STATE.stop();
					stopOpening = false;
				} else {
					if (!stopOpening) {
						//CHEVRON_LOCK_STATE.stop();
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
