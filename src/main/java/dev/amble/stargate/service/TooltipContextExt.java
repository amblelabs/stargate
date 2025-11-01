package dev.amble.stargate.service;

import net.minecraft.client.item.TooltipContext;

// TODO: move to Amble
public interface TooltipContextExt extends TooltipContext {
    boolean isShiftDown();
    boolean isAltDown();
    boolean isControlDown();
}
