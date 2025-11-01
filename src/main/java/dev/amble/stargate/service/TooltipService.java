package dev.amble.stargate.service;

import net.minecraft.client.item.TooltipContext;

public abstract class TooltipService {

    public static TooltipService INSTANCE;

    public abstract TooltipContextExt create(TooltipContext context);
}
