package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.blocks.*;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class StargateBlockEntities {
    public static void registerTiles(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
        for (var e : BLOCK_ENTITIES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITIES = new LinkedHashMap<>();

    public static final BlockEntityType<StargateBlockEntity> STARGATE = register("stargate", StargateBlockEntity::new, StargateBlocks.STARGATE);

    public static final BlockEntityType<StargateRingBlockEntity> RING = register("ring", StargateRingBlockEntity::new, StargateBlocks.RING);

    public static final BlockEntityType<ToasterBlockEntity> TOASTER = register("toaster", ToasterBlockEntity::new, StargateBlocks.TOASTER);

    public static final BlockEntityType<DHDBlockEntity> DHD = register("dhd_block_entity", DHDBlockEntity::new, StargateBlocks.DHD_BLOCK);

    private static <T extends BlockEntity> BlockEntityType<T> register(String id,
                                                                       BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        var ret = XplatAbstractions.INSTANCE.createBlockEntityType(func, blocks);

        var old = BLOCK_ENTITIES.put(StargateAPI.modLoc(id), ret);
        if (old != null) {
            throw new IllegalArgumentException("Duplicate id " + id);
        }
        return ret;
    }
}
