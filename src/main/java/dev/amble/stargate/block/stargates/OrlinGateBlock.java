package dev.amble.stargate.block.stargates;

import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.GateKernelRegistry;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateSounds;
import dev.amble.stargate.util.VoxelShapeUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"deprecation", "UnstableApiUsage"})
public class OrlinGateBlock extends StargateBlock {

    private static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0F, 0.0F, 0.0F, 16.0F / 16, 8.0F / 16f, 1.0f);

    public OrlinGateBlock(ABlockSettings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapeUtil.rotateShapeH(Direction.NORTH, HorizontalBlockBehavior.getFacing(state), SHAPE);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapeUtil.rotateShapeH(Direction.NORTH, HorizontalBlockBehavior.getFacing(state), SHAPE);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);

        if (world instanceof ServerWorld serverWorld && world.getBlockEntity(pos) instanceof StargateBlockEntity sg) {
            sg.onPlacedWithKernel(serverWorld, state, GateKernelRegistry.ORLIN);
            world.playSound(null, pos, StargateSounds.DING, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }
}
