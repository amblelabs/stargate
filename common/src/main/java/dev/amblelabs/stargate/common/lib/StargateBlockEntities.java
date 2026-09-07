package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.blocks.*;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class StargateBlockEntities {

    private static final XplatRegister<BlockEntityType<?>> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.BLOCK_ENTITY_TYPE);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Supplier<BlockEntityType<StargateBlockEntity>> STARGATE = register("stargate", StargateBlockEntity::new, StargateBlocks.STARGATE);

    public static final Supplier<BlockEntityType<StargateRingBlockEntity>> RING = register("ring", StargateRingBlockEntity::new, StargateBlocks.RING);

    public static final Supplier<BlockEntityType<ToasterBlockEntity>> TOASTER = register("toaster", ToasterBlockEntity::new, StargateBlocks.TOASTER);

    public static final Supplier<BlockEntityType<DHDBlockEntity>> DHD = register("dhd_block_entity", DHDBlockEntity::new, StargateBlocks.DHD_BLOCK);

    @SafeVarargs
    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String id, BiFunction<BlockPos, BlockState, T> func, Supplier<? extends Block>... blocks) {
        return REGISTER.register(id, () -> XplatAbstractions.INSTANCE.createBlockEntityType(func,
                Arrays.stream(blocks).map(Supplier::get).toArray(Block[]::new)
        ));
    }
}
