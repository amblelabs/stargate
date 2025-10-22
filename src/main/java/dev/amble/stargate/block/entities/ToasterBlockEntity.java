package dev.amble.stargate.block.entities;

import dev.amble.lib.blockentity.ABlockEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateItems;
import dev.amble.stargate.init.StargateSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ToasterBlockEntity extends ABlockEntity {

    private int cookingTicks;
    private ItemStack heldItem = ItemStack.EMPTY;

    public ToasterBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.TOASTER, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("cookingTicks", cookingTicks);
        nbt.put("heldItem", heldItem.writeNbt(new NbtCompound()));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.cookingTicks = nbt.getInt("cookingTicks");
        this.heldItem = ItemStack.fromNbt(nbt.getCompound("heldItem"));
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state) {
        if (this.cookingTicks > 0) {
            this.cookingTicks--;
            return;
        }

        ItemStack result = this.cookItem();

        Vec3d spawnPosition = pos.toCenterPos().add(0, 0.4, 0);
        ItemEntity toastEntity = new ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, result);

        world.spawnEntity(toastEntity);

        world.playSound(
                null,
                pos,
                StargateSounds.TOASTER,
                SoundCategory.BLOCKS,
                0.75F,
                1.0F
        );

        world.playSound(
                null,
                pos,
                StargateSounds.DING,
                SoundCategory.BLOCKS,
                1.0F,
                1.0F
        );

        this.heldItem = ItemStack.EMPTY;
        this.cookingTicks = 0;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        this.cookingTicks = 10;
        this.heldItem = stack.copyAndEmpty();

        world.playSound(
                null,
                pos,
                StargateSounds.TOASTER,
                SoundCategory.BLOCKS,
                1.0F,
                1.5F
        );

        world.playSound(
                null,
                pos,
                StargateSounds.TOASTER_ACTIVE,
                SoundCategory.BLOCKS,
                1.0F,
                1.0F
        );

        return ActionResult.PASS;
    }

    private ItemStack cookItem() {
        if (heldItem.getItem() == Items.BREAD) return new ItemStack(StargateItems.TOAST);
        if (heldItem.getItem() == StargateItems.TOAST) return new ItemStack(StargateItems.BURNT_TOAST);

        return heldItem;
    }
}
