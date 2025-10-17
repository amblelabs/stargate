package dev.amble.stargate.block.entities;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.address.GlyphOriginRegistry;
import dev.amble.stargate.api.dhd.SymbolArrangement;
import dev.amble.stargate.api.dhd.control.SymbolControl;
import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.entities.DHDControlEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.item.StargateLinkableItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DHDBlockEntity extends NearestLinkingBlockEntity {

    public final List<DHDControlEntity> symbolControlEntities = new ArrayList<>();
    private boolean needsSymbols = true;

    public DHDBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.DHD, pos, state, true);
    }

    @Override
    public boolean link(Stargate gate) {
        this.markNeedsControl();
        return super.link(gate);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        this.markNeedsControl();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() instanceof StargateLinkableItem || hand != Hand.MAIN_HAND)
            return ActionResult.PASS;

        if (!this.isLinked()) return ActionResult.FAIL;
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
        return super.toInitialChunkDataNbt();
    }

    @Override
    public void markRemoved() {
        this.killControls();
        super.markRemoved();
    }

    @Override
    public void onBreak(BlockState state, World world, BlockPos pos, BlockState newState) {
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

        Stargate stargate = this.asGate();
        if (stargate == null) return;

        List<SymbolArrangement> controls = DHDControlEntity.getSymbolArrangement();
        Direction direction = HorizontalBlockBehavior.getFacing(this.world.getBlockState(this.getPos()));

        for (SymbolArrangement control : controls) {
            DHDControlEntity controlEntity = DHDControlEntity.create(this.world, stargate);

            Vec3d position = current.toCenterPos();
            Vector3f offset = control.getOffset();

            // Flip horizontally if facing north or south
            if (direction == Direction.NORTH || direction == Direction.SOUTH)
                offset = new Vector3f(-offset.x(), offset.y(), offset.z());

            controlEntity.setPosition(
                    position.x + offset.x() * direction.getOffsetZ() - offset.z() * direction.getOffsetX(),
                    position.y + offset.y(),
                    position.z + offset.x() * direction.getOffsetX() - offset.z() * direction.getOffsetZ()
            );

            controlEntity.setYaw(0.0f);
            controlEntity.setPitch(0.0f);

            controlEntity.setControlData(control, this.getPos());

            serverWorld.spawnEntity(controlEntity);
            this.symbolControlEntities.add(controlEntity);
        }

        DHDControlEntity dialButtonEntity = DHDControlEntity.create(this.world, stargate);
        dialButtonEntity.setPosition(current.getX() + 0.5, current.getY() + 0.9625f, current.getZ() + 0.5);
        dialButtonEntity.setYaw(0.0f);
        dialButtonEntity.setPitch(0.0f);

        char poi = GlyphOriginRegistry.get().glyph(world.getRegistryKey());

        dialButtonEntity.setControlData(new SymbolArrangement(new SymbolControl(poi),
                EntityDimensions.fixed(0.2f, 0.2f), new Vector3f(0, 0, 0)), this.getPos());

        serverWorld.spawnEntity(dialButtonEntity);
        this.symbolControlEntities.add(dialButtonEntity);

        this.needsSymbols = false;
    }

    public void markNeedsControl() {
        this.needsSymbols = true;
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
        if (this.needsSymbols)
            this.spawnControls();
    }
}
