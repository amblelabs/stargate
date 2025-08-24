package dev.amble.stargate.init;

import dev.amble.lib.container.impl.BlockEntityContainer;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.block.entities.StargateRingBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class StargateBlockEntities implements BlockEntityContainer {
	public static BlockEntityType<StargateBlockEntity> MILKY_WAY_STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.MILKY_WAY_STARGATE).build();
	public static BlockEntityType<StargateBlockEntity> ORLIN_STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.ORLIN_STARGATE).build();
	public static BlockEntityType<StargateBlockEntity> PEGASUS_STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.PEGASUS_STARGATE).build();
	public static BlockEntityType<StargateBlockEntity> DESTINY_STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.DESTINY_STARGATE).build();
	public static BlockEntityType<DHDBlockEntity> DHD = FabricBlockEntityTypeBuilder.create(DHDBlockEntity::new, StargateBlocks.DHD).build();
	public static BlockEntityType<StargateRingBlockEntity> RING = FabricBlockEntityTypeBuilder.create(StargateRingBlockEntity::new, StargateBlocks.RING).build();
}
