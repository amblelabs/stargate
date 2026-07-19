package dev.amblelabs.stargate.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class StargateRingBlock extends BaseEntityBlock {

    public static final MapCodec<StargateRingBlock> CODEC = simpleCodec(StargateRingBlock::new);

    public StargateRingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof StargateRingBlockEntity ring))
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        if (stack.isEmpty()) {
            ring.setBlockSet(null);
            return ItemInteractionResult.SUCCESS;
        }

        if (!(stack.getItem() instanceof BlockItem blockItem))
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        if (blockItem.getBlock().defaultBlockState() != ring.getBlockSet()) {
            ring.setBlockSet(blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(player, hand, stack, hitResult)));
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StargateRingBlockEntity(pos, state);
    }
}
