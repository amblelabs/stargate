package dev.amble.stargate.api.v3.behavior;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.StargateTpEvent;
import dev.amble.stargate.init.StargateCriterions;
import dev.drtheo.yaar.behavior.TBehavior;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class StargateTpEffectsBehavior implements TBehavior, StargateEvents {

    private static Advancement PASSED_THROUGH_GATE;

    private static Advancement getPassThroughAdv() {
        if (PASSED_THROUGH_GATE != null)
            return PASSED_THROUGH_GATE;

        return PASSED_THROUGH_GATE = ServerLifecycleHooks.get().getAdvancementLoader().get(StargateMod.id("passed_through"));
    }

    public static boolean hasPassedThroughBefore(ServerPlayerEntity player) {
        return player.getAdvancementTracker().getProgress(getPassThroughAdv()).isDone();
    }

    @Override
    public StargateTpEvent.Result onGateTp(Stargate from, Stargate to, LivingEntity living) {
        if (living instanceof ServerPlayerEntity player && !hasPassedThroughBefore(player)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 4,
                    2, false, false, true));

            StargateCriterions.PASSED_THROUGH.trigger(player);
        }

        if (living.canFreeze())
            living.setFrozenTicks(100);

        return StargateTpEvent.Result.PASS;
    }
}
