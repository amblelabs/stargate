package dev.amble.stargate.client.service;

import dev.amble.stargate.service.TooltipContextExt;
import dev.amble.stargate.service.TooltipService;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;

public class TooltipServiceImpl extends TooltipService {

    @Override
    public TooltipContextExt create(TooltipContext context) {
        return new TooltipContextExtImpl(context);
    }

    private record TooltipContextExtImpl(TooltipContext ctx) implements TooltipContextExt {

        @Override
            public boolean isShiftDown() {
                return Screen.hasShiftDown();
            }

            @Override
            public boolean isAltDown() {
                return Screen.hasAltDown();
            }

            @Override
            public boolean isControlDown() {
                return Screen.hasControlDown();
            }

            @Override
            public boolean isAdvanced() {
                return ctx.isAdvanced();
            }

            @Override
            public boolean isCreative() {
                return ctx.isCreative();
            }
        }
}
