package dev.amble.stargate.mixins;

import dev.amble.stargate.api.TeleportableEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements TeleportableEntity {

    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        this.stargate$setStatus(((TeleportableEntity) oldPlayer).stargate$status());
    }
}
