package dev.amble.stargate.mixins;

import dev.amble.stargate.api.util.TeleportableEntity;
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
    private int stargate$ticks;

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void addAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(StargateAttributes.SPACIAL_RESISTANCE);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        this.stargate$tickTicks();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void toNbt(NbtCompound nbt, CallbackInfo ci) {
        if (stargate$ticks != 0)
            nbt.putInt("StargateTpState", stargate$ticks);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    public void fromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.stargate$ticks = nbt.getInt("StargateTpState");
    }

    @Override
    public int stargate$ticks() {
        return stargate$ticks;
    }

    @Override
    public void stargate$setTicks(int ticks) {
        this.stargate$ticks = ticks;
    }
}
