package dev.amblelabs.stargate.fabric.datagen.tag;

import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.xplat.IXplatTags;
import dev.amblelabs.lib.fabric.datagen.FabricAmbleBlockTagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class StargateBlockTagProvider extends FabricAmbleBlockTagProvider {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final IXplatTags xtags;

    public StargateBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, IXplatTags xtags) {
        super(output, lookupProvider);
        this.xtags = xtags;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(StargateBlocks.NAQUADAH_ORE)
                .add(StargateBlocks.RAW_NAQUADAH_BLOCK)
                .add(StargateBlocks.NAQUADAH_BLOCK)
                .add(StargateBlocks.SANDSTONE_BRICKS)
                .add(StargateBlocks.SANDSTONE_BRICK_SLAB)
                .add(StargateBlocks.SANDSTONE_BRICK_STAIRS)
                .add(StargateBlocks.SANDSTONE_BRICK_WALL);

        this.getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(StargateBlocks.NAQUADAH_ORE)
                .add(StargateBlocks.RAW_NAQUADAH_BLOCK)
                .add(StargateBlocks.NAQUADAH_BLOCK);

        this.getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(StargateBlocks.SANDSTONE_BRICKS)
                .add(StargateBlocks.SANDSTONE_BRICK_SLAB)
                .add(StargateBlocks.SANDSTONE_BRICK_STAIRS)
                .add(StargateBlocks.SANDSTONE_BRICK_WALL);

        this.getOrCreateTagBuilder(BlockTags.WALLS)
                .add(StargateBlocks.SANDSTONE_BRICK_WALL);
    }
}
