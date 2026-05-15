package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateRecipes;
import dev.amblelabs.stargate.common.lib.StargateSounds;
import dev.amblelabs.stargate.common.recipe.ToastingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ToasterBlockEntity extends BlockEntity {

    private static final String TAG_ITEM = "Item";
    private static final String TAG_COOKING_PROGRESS = "CookingTime";
    private static final String TAG_COOKING_TIME = "CookingTotalTime";

    private final RecipeManager.CachedCheck<SingleRecipeInput, ToastingRecipe> quickCheck;

    private ItemStack heldItem = ItemStack.EMPTY;

    private int cookingTime;
    private int cookingProgress;

    public ToasterBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.TOASTER, pos, state);
        this.quickCheck = RecipeManager.createCheck(StargateRecipes.TOASTING);
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public Optional<RecipeHolder<ToastingRecipe>> getToastableRecipe(ItemStack stack) {
        return this.heldItem.isEmpty() ? Optional.empty() : this.quickCheck.getRecipeFor(new SingleRecipeInput(stack), this.level);
    }

    public boolean placeFood(@Nullable LivingEntity entity, ItemStack stack, int cookTime) {
        if (!this.heldItem.isEmpty())
            return false;

        this.cookingTime = cookTime;
        this.cookingProgress = 0;

        this.heldItem = stack.consumeAndReturn(1, entity);
        this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(entity, this.getBlockState()));
        this.markUpdated();

        return true;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        this.heldItem = ItemStack.parse(registries, tag.getCompound(TAG_ITEM)).orElse(ItemStack.EMPTY);

        if (tag.contains(TAG_COOKING_PROGRESS, CompoundTag.TAG_INT))
            this.cookingProgress = tag.getInt(TAG_COOKING_PROGRESS);

        if (tag.contains(TAG_COOKING_TIME, CompoundTag.TAG_INT))
            this.cookingTime = tag.getInt(TAG_COOKING_TIME);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put(TAG_ITEM, this.heldItem.save(registries, new CompoundTag()));

        tag.putInt(TAG_COOKING_PROGRESS, this.cookingProgress);
        tag.putInt(TAG_COOKING_TIME, this.cookingTime);
    }

    public static void cookTick(Level level, BlockPos pos, BlockState state, ToasterBlockEntity blockEntity) {
        if (blockEntity.heldItem.isEmpty())
            return;

        blockEntity.cookingProgress++;

        if (blockEntity.cookingProgress >= blockEntity.cookingTime) {
            SingleRecipeInput singleRecipeInput = new SingleRecipeInput(blockEntity.heldItem);
            ItemStack result = blockEntity.quickCheck.getRecipeFor(singleRecipeInput, level)
                    .map((recipeHolder) -> recipeHolder.value().assemble(singleRecipeInput, level.registryAccess()))
                    .orElse(blockEntity.heldItem);

            if (result.isItemEnabled(level.enabledFeatures())) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
                blockEntity.heldItem = ItemStack.EMPTY;

                state.setValue(ToasterBlock.ACTIVE, false);

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

                level.sendBlockUpdated(pos, state, state, StargateBlock.UPDATE_ALL);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
            }
        }

        setChanged(level, pos, state);
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}