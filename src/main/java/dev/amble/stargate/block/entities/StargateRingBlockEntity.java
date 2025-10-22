package dev.amble.stargate.block.entities;

import dev.amble.lib.blockentity.ABlockEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StargateRingBlockEntity extends ABlockEntity {

    private BlockState blockSet;

    public StargateRingBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.RING, pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking()) return ActionResult.PASS;

        ItemStack heldItem = player.getStackInHand(hand);

        if (heldItem.isEmpty()) {
            this.setBlockSet(null);
            this.markDirty();
            return ActionResult.SUCCESS;
        }

        if (!(heldItem.getItem() instanceof BlockItem blockItem))
            return ActionResult.PASS;

        if (blockItem.getBlock().getDefaultState() != this.getBlockSet()) {
            this.setBlockSet(blockItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, player.getMainHandStack(), hit)));
            this.markDirty();

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (this.blockSet != null) nbt.put("blockSet", NbtHelper.fromBlockState(this.blockSet));

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.blockSet = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockSet")).result().orElse(null);

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
}