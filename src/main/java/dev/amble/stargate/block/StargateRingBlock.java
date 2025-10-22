package dev.amble.stargate.block;

import dev.amble.lib.api.ICantBreak;
import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.AWaterloggableBlock;
import dev.amble.lib.block.behavior.InvisibleBlockBehavior;
import dev.amble.lib.block.behavior.base.BlockWithEntityBehavior;
import dev.amble.stargate.block.entities.StargateRingBlockEntity;
import net.minecraft.block.*;

@SuppressWarnings("UnstableApiUsage")
public class StargateRingBlock extends AWaterloggableBlock implements BlockEntityProvider, ICantBreak {

	public StargateRingBlock(ABlockSettings settings) {
		super(settings,
				new InvisibleBlockBehavior(), new BlockWithEntityBehavior(StargateRingBlockEntity::new)
		);
	}
}
