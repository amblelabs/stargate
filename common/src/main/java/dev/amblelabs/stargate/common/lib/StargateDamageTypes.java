package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StargateDamageTypes {
    public static final ResourceKey<DamageType> KAWOOSH = type("kawoosh");
    public static final ResourceKey<DamageType> IRIS = type("iris");
    public static final ResourceKey<DamageType> FLOW = type("flow");

    private static ResourceKey<DamageType> type(ResourceLocation id) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, id);
    }

    private static ResourceKey<DamageType> type(String name) {
        return type(StargateAPI.modLoc(name));
    }

    public static DamageSource source(Level level, ResourceKey<DamageType> damageTypeKey) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey));
    }

    public static DamageSource source(Level level, ResourceKey<DamageType> damageTypeKey, @Nullable Entity entity) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey), entity);
    }

    public static DamageSource source(Level level, ResourceKey<DamageType> damageTypeKey, @Nullable Entity causingEntity, @Nullable Entity directEntity) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey), causingEntity, directEntity);
    }
}
