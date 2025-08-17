package dev.amble.stargate.mixins;

import dev.amble.stargate.api.TeleportableEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements TeleportableEntity {

    @Shadow private boolean inTeleportationState;

    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        this.stargate$setTicks(((TeleportableEntity) oldPlayer).stargate$ticks());
    }

    @Override
    public void stargate$tickTicks() {
        if (this.inTeleportationState)
            return;

        TeleportableEntity.super.stargate$tickTicks();
    }
}
