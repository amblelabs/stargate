package dev.amblelabs.stargate.mixin.stargate_manager;

import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import dev.amblelabs.stargate.client.api.stargate.ClientStargateNetwork;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class ClientLevelMixin implements StargateNetwork.ManagerLevel {

    @Unique
    private @Nullable ClientStargateNetwork stargate$network;

    @Override
    public @NotNull StargateNetwork stargate$getNetwork() {
        return this.stargate$network != null ? this.stargate$network
                : (this.stargate$network = new ClientStargateNetwork((ClientLevel) (Object) this));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (this.stargate$network == null) return;
        this.stargate$network.tick();
    }
}
