package dev.amble.stargate.init;

import dev.amble.stargate.StargateMod;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class StargateDamages {

    public static final RegistryKey<DamageType> FLOW = of("flow");

    public static DamageSource flow(World world) {
        return of(world, FLOW);
    }

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

    private static RegistryKey<DamageType> of(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, StargateMod.id(name));
    }
}
