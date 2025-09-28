package dev.amble.stargate.block.entities;

import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.block.DHDBlock;
import dev.amble.stargate.dhd.DHDArrangement;
import dev.amble.stargate.dhd.SymbolArrangement;
import dev.amble.stargate.dhd.control.SymbolControl;
import dev.amble.stargate.entities.DHDControlEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DHDBlockEntity extends NearestLinkingBlockEntity implements BlockEntityTicker<DHDBlockEntity> {
    public final List<DHDControlEntity> symbolControlEntities = new ArrayList<>();
    private boolean needsSymbols = true;
    public DHDBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.DHD, pos, state, true);
    }

    @Override
    public void onLinked() {
        super.onLinked();
        this.markNeedsControl();
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!this.hasStargate()) return ActionResult.FAIL;
        //if (world.isClient()) return ActionResult.SUCCESS;

        /*Stargate target = this.gate().get();

        if (target.state() instanceof GateState.Open || target.state() instanceof GateState.PreOpen) return ActionResult.FAIL;

        player.sendMessage(target.address().asText(), true);

        if (target.state() instanceof GateState.Closed closed)
            closed.setAddress(target.address().text());*/

        return ActionResult.SUCCESS;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        this.markNeedsControl();
        return createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void markRemoved() {
        this.killControls();
        super.markRemoved();
    }

    public void onBroken() {
        this.killControls();
    }

    public void killControls() {
        symbolControlEntities.forEach(Entity::discard);
        symbolControlEntities.clear();
    }

    public void spawnControls() {
        BlockPos current = this.getPos();

        if (!(this.world instanceof ServerWorld serverWorld))
            return;

        this.killControls();

        if (!this.isLinked()) return;

        DHDArrangement.reloadArrangement(Glyph.ALL);

        List<SymbolArrangement> controls = DHDControlEntity.getSymbolArrangement();

        for (SymbolArrangement control : controls) {
            DHDControlEntity controlEntity = DHDControlEntity.create(this.world, this.gate().get());

            Vector3f position = current.toCenterPos().toVector3f();
            Direction direction = this.world.getBlockState(this.getPos()).get(DHDBlock.FACING);

            Vector3f offset = control.getOffset();
            // Flip horizontally if facing north or south
            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                offset = new Vector3f(-offset.x(), offset.y(), offset.z());
            }

            position = new Vector3f(
                    position.x + offset.x() * direction.getOffsetZ() + (-offset.z() * direction.getOffsetX()),
                    position.y + offset.y(),
                    position.z + offset.x() * direction.getOffsetX() - (offset.z() * direction.getOffsetZ())
            );
            controlEntity.setPosition(position.x(), position.y(), position.z());
            controlEntity.setYaw(0.0f);
            controlEntity.setPitch(0.0f);

            controlEntity.setControlData(control, this.getPos());

            serverWorld.spawnEntity(controlEntity);
            this.symbolControlEntities.add(controlEntity);
        }

        DHDControlEntity dialButtonEntity = DHDControlEntity.create(this.world, this.gate().get());
        dialButtonEntity.setPosition(current.getX() + 0.5, current.getY() + 0.9625015230849385f, current.getZ() + 0.5);
        dialButtonEntity.setYaw(0.0f);
        dialButtonEntity.setPitch(0.0f);
        dialButtonEntity.setControlData(new SymbolArrangement(new SymbolControl('*'),
                EntityDimensions.fixed(0.2f, 0.2f), new Vector3f(0, 0, 0)), this.getPos());

        serverWorld.spawnEntity(dialButtonEntity);
        this.symbolControlEntities.add(dialButtonEntity);

        this.needsSymbols = false;
    }

    public void markNeedsControl() {
        this.needsSymbols = true;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, DHDBlockEntity blockEntity) {
        if (this.needsSymbols)
            this.spawnControls();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }
}
