package dev.amblelabs.stargate.common.worldgen;

import dev.amblelabs.lib.api.util.MutableBlockPos;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.Prototype;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.items.StargateBlockItem;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class BuriedStargatePieces {

    public static class BuriedStargatePiece extends StructurePiece {
        public BuriedStargatePiece(BlockPos pos) {
            super(StargateStructurePieces.BURIED_STARGATE, 0, new BoundingBox(pos));
        }

        public BuriedStargatePiece(CompoundTag tag) {
            super(StargateStructurePieces.BURIED_STARGATE, tag);
        }

        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        }

        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            int i = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.boundingBox.minX(), this.boundingBox.minZ());
            MutableBlockPos mutableBlockPos = new MutableBlockPos(this.boundingBox.minX(), i, this.boundingBox.minZ());

            while (mutableBlockPos.getY() > level.getMinBuildHeight()) {
                BlockState blockState = level.getBlockState(mutableBlockPos);
                BlockState blockState2 = level.getBlockState(mutableBlockPos.below());
                if (blockState2 == Blocks.SANDSTONE.defaultBlockState() || blockState2 == Blocks.STONE.defaultBlockState() || blockState2 == Blocks.ANDESITE.defaultBlockState() || blockState2 == Blocks.GRANITE.defaultBlockState() || blockState2 == Blocks.DIORITE.defaultBlockState()) {
                    BlockState blockState3 = !blockState.isAir() && !this.isLiquid(blockState) ? blockState : Blocks.SAND.defaultBlockState();

                    for(Direction direction : Direction.values()) {
                        BlockPos blockPos = mutableBlockPos.relative(direction);
                        BlockState blockState4 = level.getBlockState(blockPos);
                        if (blockState4.isAir() || this.isLiquid(blockState4)) {
                            BlockPos blockPos2 = blockPos.below();
                            BlockState blockState5 = level.getBlockState(blockPos2);
                            if ((blockState5.isAir() || this.isLiquid(blockState5)) && direction != Direction.UP) {
                                level.setBlock(blockPos, blockState2, Block.UPDATE_ALL);
                            } else {
                                level.setBlock(blockPos, blockState3, Block.UPDATE_ALL);
                            }
                        }
                    }

                    mutableBlockPos.below(random.nextIntBetweenInclusive(0, 6));
                    this.boundingBox = new BoundingBox(mutableBlockPos);

                    Direction facing = Direction.values()[2 + random.nextInt(4)];

                    level.setBlock(mutableBlockPos, StargateBlocks.STARGATE.defaultBlockState().setValue(StargateBlock.FACING, facing), Block.UPDATE_CLIENTS);

                    if (!(level.getBlockEntity(mutableBlockPos) instanceof StargateBlockEntity blockEntity)) return;

                    Prototype entry = StargateBlockItem.pickRandomPrototype(level.getRandom()).value();
                    Stargate stargate = ServerStargateNetwork.get(level.getLevel()).create(entry);

                    blockEntity.setStargate(stargate);

                    StargateBlockEvents.notify(events -> events.stargate$place(
                            stargate, blockEntity, level.getBlockState(mutableBlockPos), level, mutableBlockPos));

                    stargate.setChanged(); // force sync
                    return;
                }

                mutableBlockPos.below();
            }
        }

        private boolean isLiquid(BlockState state) {
            return state == Blocks.WATER.defaultBlockState() || state == Blocks.LAVA.defaultBlockState();
        }
    }
}
