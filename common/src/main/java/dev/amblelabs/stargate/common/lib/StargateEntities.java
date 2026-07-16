package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.entities.DHDControlEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class StargateEntities {

    public static void registerEntities(BiConsumer<EntityType<?>, ResourceLocation> r) {
        for (var e : ENTITIES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, EntityType<?>> ENTITIES = new LinkedHashMap<>();

    public static final EntityType<DHDControlEntity> DHD_CONTROL = register("dhd_control",
            EntityType.Builder.<DHDControlEntity>of(DHDControlEntity::new, MobCategory.MISC)
                    .sized(0.125F, 0.125F).noSummon().build("dhd_control"));

    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
        var old = ENTITIES.put(StargateAPI.modLoc(id), type);
        if (old != null) {
            throw new IllegalArgumentException("Duplicate id " + id);
        }
        return type;
    }
}
