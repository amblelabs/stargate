package dev.amble.stargate.init;

import dev.amble.lib.container.impl.BlockEntityContainer;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.block.entities.StargateRingBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class StargateBlockEntities implements BlockEntityContainer {
	public static BlockEntityType<StargateBlockEntity> GENERIC_STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.GENERIC_GATE).build();
	public static BlockEntityType<StargateBlockEntity> ORLIN_GATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.ORLIN_GATE).build();
	public static BlockEntityType<DHDBlockEntity> DHD = FabricBlockEntityTypeBuilder.create(DHDBlockEntity::new, StargateBlocks.DHD).build();
	public static BlockEntityType<StargateRingBlockEntity> RING = FabricBlockEntityTypeBuilder.create(StargateRingBlockEntity::new, StargateBlocks.RING).build();
}
