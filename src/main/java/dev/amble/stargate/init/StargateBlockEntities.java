package dev.amble.stargate.init;

import dev.amble.lib.container.impl.BlockEntityContainer;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class StargateBlockEntities implements BlockEntityContainer {
	public static BlockEntityType<StargateBlockEntity> STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.STARGATE).build();
	public static BlockEntityType<DHDBlockEntity> DHD = FabricBlockEntityTypeBuilder.create(DHDBlockEntity::new, StargateBlocks.DHD).build();
}
