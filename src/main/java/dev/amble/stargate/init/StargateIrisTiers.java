package dev.amble.stargate.init;

import dev.amble.stargate.api.IrisTierContainer;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.behavior.IrisBehavior;
import dev.amble.stargate.api.state.IrisTier;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.world.ServerWorld;

public class StargateIrisTiers implements IrisTierContainer {

    public static IrisTier IRON = new IrisTier.Impl() {
        @Override
        public int maxDurability() {
            return 1000;
        }
    };

    public static IrisTier GOLD = new IrisTier.Impl() {
        @Override
        public int maxDurability() {
            return 1;
        }

        @Override
        public void onBroken(IrisBehavior.IrisDamageCtx ctx) {
            Stargate stargate = ctx.stargate();
            PlayerLookup.tracking((ServerWorld) stargate.world(), stargate.pos()).forEach(player -> {
                StargateCriterions.IRIS_BREAK.trigger(player, this);
            });
        }
    };

    public static IrisTier getDefault() {
        return REGISTRY.get(REGISTRY.getDefaultId());
    }
}
