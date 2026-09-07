package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.entities.DHDControlEntity;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class StargateEntities {

    private static final XplatRegister<EntityType<?>> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.ENTITY_TYPE);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Supplier<EntityType<DHDControlEntity>> DHD_CONTROL = type("dhd_control", DHDControlEntity::new, MobCategory.MISC,
            builder -> builder.sized(0.125F, 0.125F).noSummon());

    @SuppressWarnings("SameParameterValue")
    private static <T extends Entity> Supplier<EntityType<T>> type(String id, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> op) {
        return type(id, () -> op.apply(EntityType.Builder.of(factory, category)).build(StargateAPI.MOD_ID + ":" + id));
    }

    private static <T extends Entity> Supplier<EntityType<T>> type(String id, Supplier<EntityType<T>> type) {
        return REGISTER.register(StargateAPI.modLoc(id), type);
    }
}
