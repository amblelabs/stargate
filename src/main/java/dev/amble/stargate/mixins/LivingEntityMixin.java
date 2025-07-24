package dev.amble.stargate.mixins;

import dev.amble.stargate.api.TeleportableEntity;
import dev.amble.stargate.init.StargateAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TeleportableEntity {

    @Unique
    private State stargate$state = State.OUTSIDE;

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void addAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(StargateAttributes.SPACIAL_RESISTANCE);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        if (this.stargate$state.isInGate())
            this.stargate$state = stargate$state.next();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void toNbt(NbtCompound nbt, CallbackInfo ci) {
        if (stargate$state.isInGate())
            nbt.putInt("StargateTpState", stargate$state.ordinal());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    public void fromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.stargate$state = State.VALS[nbt.getInt("StargateTpState")];
    }

    @Override
    public void stargate$setStatus(State inGate) {
        this.stargate$state = inGate;
    }

    @Override
    public State stargate$status() {
        return stargate$state;
    }
}
