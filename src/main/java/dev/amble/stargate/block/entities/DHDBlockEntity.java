package dev.amble.stargate.block.entities;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.Glyph;
import dev.amble.stargate.api.dhd.DHDArrangement;
import dev.amble.stargate.api.dhd.SymbolArrangement;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.entities.DHDControlEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

public class DHDBlockEntity extends NearestLinkingBlockEntity {

    private final Deque<DHDControlEntity> symbolControlEntities = new ArrayDeque<>(Glyph.ALL.length);
    private boolean needsSymbols = true;

    public DHDBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.DHD, pos, state, true);
    }

    public void onUseControl(PlayerEntity player, World world, DHDControlEntity entity, boolean leftClick) {
        Stargate stargate = this.asGate();

        if (stargate == null) {
            StargateMod.LOGGER.warn("Discarding invalid control entity at {}; dhd pos: {}", entity.getPos(), this.getPos());

            entity.discard();
            return;
        }

        world.playSound(null, this.getPos(), StargateSounds.DHD_PRESS, SoundCategory.BLOCKS, 0.7f, 1f);

        GateState.Closed closed = stargate.stateOrNull(GateState.Closed.state);

        if (closed != null) {
            closed.address += entity.getSymbol();
            stargate.markDirty();
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        this.markNeedsControl();
    }

    @Override
    public void onBreak(BlockState state, World world, BlockPos pos, BlockState newState) {
        this.killControls();
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
        if (this.needsSymbols)
            this.spawnControls();
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        this.markNeedsControl();
        return super.toInitialChunkDataNbt();
    }

    @Override
    public boolean link(Stargate gate) {
        this.markNeedsControl();
        return super.link(gate);
    }

    @Override
    public void markRemoved() {
        this.killControls();
        super.markRemoved();
    }

    private void killControls() {
        while (!symbolControlEntities.isEmpty()) {
            Entity entity = symbolControlEntities.poll();
            if (entity == null) continue;

            entity.discard();
        }
    }

    private void spawnControls() {
        if (this.world.isClient()) return;

        this.killControls();

        Stargate stargate = this.asGate();
        if (stargate == null) return;

        Direction direction = HorizontalBlockBehavior.getFacing(this.world.getBlockState(this.pos));

        for (SymbolArrangement control : DHDArrangement.SYMBOLS) {
            this.symbolControlEntities.add(control.createEntity(this.world, this.pos, direction));
        }

        this.symbolControlEntities.add(DHDArrangement.poi(world).createEntity(this.world, this.pos, direction));
        this.needsSymbols = false;
    }

    public void markNeedsControl() {
        this.needsSymbols = true;
    }
}
