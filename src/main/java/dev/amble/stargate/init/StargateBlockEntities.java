package dev.amble.stargate.init;

import dev.amble.lib.container.impl.BlockEntityContainer;
import dev.amble.stargate.block.entities.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class StargateBlockEntities implements BlockEntityContainer {
	public static final BlockEntityType<StargateBlockEntity> GENERIC_STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.GENERIC_GATE).build();
	public static final BlockEntityType<DHDBlockEntity> DHD = FabricBlockEntityTypeBuilder.create(DHDBlockEntity::new, StargateBlocks.DHD).build();
	public static final BlockEntityType<StargateRingBlockEntity> RING = FabricBlockEntityTypeBuilder.create(StargateRingBlockEntity::new, StargateBlocks.RING).build();
	public static final BlockEntityType<ToasterBlockEntity> TOASTER = FabricBlockEntityTypeBuilder.create(ToasterBlockEntity::new, StargateBlocks.TOASTER).build();
	public static final BlockEntityType<DialingComputerBlockEntity> COMPUTER = FabricBlockEntityTypeBuilder.create(DialingComputerBlockEntity::new, StargateBlocks.COMPUTER).build();
}
