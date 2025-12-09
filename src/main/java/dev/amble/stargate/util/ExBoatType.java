package dev.amble.stargate.util;

import dev.amble.lib.util.LazyObject;
import net.minecraft.entity.vehicle.BoatEntity;

public interface ExBoatType {

    LazyObject<BoatEntity.Type> NAQUADAH = new LazyObject<>(() -> BoatEntity.Type.getType("naquadah"));
}
