package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateItems;
import dev.amblelabs.stargate.common.lib.StargateSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ToasterBlockEntity extends BlockEntity {

    private int cookingTicks;
    private ItemStack heldItem = ItemStack.EMPTY;

    public ToasterBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.TOASTER, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("cookingTicks", cookingTicks);
        tag.put("heldItem", heldItem.save(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        this.cookingTicks = tag.getInt("cookingTicks");
        this.heldItem = ItemStack.parse(registries, tag.getCompound("heldItem")).orElse(ItemStack.EMPTY);
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (this.heldItem.isEmpty()) return;

        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), this.heldItem);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (this.cookingTicks > 0) {
            this.cookingTicks--;
            return;
        }

        ItemStack result = this.cookItem();
        Vec3 spawnPosition = pos.getCenter().add(0, 0.4, 0);

        Containers.dropItemStack(level, spawnPosition.x, spawnPosition.y, spawnPosition.z, result);

        level.playSound(
                null,
                pos,
                StargateSounds.TOASTER_LOAD,
                SoundSource.BLOCKS,
                0.75F,
                1.0F
        );

        level.playSound(
                null,
                pos,
                StargateSounds.TOASTER_DING,
                SoundSource.BLOCKS
        );

        this.heldItem = ItemStack.EMPTY;
        this.cookingTicks = 0;
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        this.cookingTicks = 10;
        this.heldItem = stack.copyAndClear();

        world.playSound(
                null,
                pos,
                StargateSounds.TOASTER_LOAD,
                SoundSource.BLOCKS,
                1.0F,
                1.5F
        );

        world.playSound(
                null,
                pos,
                StargateSounds.TOASTER_ACTIVE,
                SoundSource.BLOCKS
        );

        return ItemInteractionResult.SUCCESS;
    }

    private ItemStack cookItem() {
        if (heldItem.getItem() == Items.BREAD) return new ItemStack(StargateItems.TOAST);
        if (heldItem.getItem() == StargateItems.TOAST) return new ItemStack(StargateItems.BURNT_TOAST);

        return heldItem;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ToasterBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }
}