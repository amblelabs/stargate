package dev.amble.stargate.init;

import dev.amble.lib.container.impl.EntityContainer;
import dev.amble.stargate.entities.DHDControlEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class StargateEntities implements EntityContainer {

    public static final EntityType<DHDControlEntity> DHD_CONTROL_TYPE = FabricEntityTypeBuilder
            .<DHDControlEntity>create(SpawnGroup.MISC, DHDControlEntity::new)
            .dimensions(EntityDimensions.changing(0.125f, 0.125f))
            .build();
}
