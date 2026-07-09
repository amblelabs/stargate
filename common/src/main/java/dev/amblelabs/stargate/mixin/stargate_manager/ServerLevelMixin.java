package dev.amblelabs.stargate.mixin.stargate_manager;

import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements StargateNetwork.ManagerLevel {

    @Unique
    private @Nullable ServerStargateNetwork stargate$network;

    @Override
    public @NotNull StargateNetwork stargate$getNetwork() {
        return this.stargate$network != null ? this.stargate$network
                : (this.stargate$network = new ServerStargateNetwork((ServerLevel) (Object) this));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        this.stargate$network.tick();
    }
}
