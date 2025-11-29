package dev.amble.stargate.service;

import net.minecraft.client.item.TooltipContext;

public interface TooltipService {

    default TooltipContextExt create(TooltipContext context) {
        throw new IllegalStateException("Tried to create extended tooltip context on server!");
    }
}
