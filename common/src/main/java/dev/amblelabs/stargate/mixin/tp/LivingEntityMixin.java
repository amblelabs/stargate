package dev.amblelabs.stargate.mixin.tp;

import dev.amblelabs.stargate.api.util.TeleportableEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TeleportableEntity {

    @Unique
    private static final String TAG_STATE = "StargateTpState";

    @Unique
    private int stargate$ticks;

//    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
//    private static void addAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
//        cir.getReturnValue().add(StargateAttributes.SPACIAL_RESISTANCE);
//    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        this.stargate$tickTicks();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void toNbt(CompoundTag compound, CallbackInfo ci) {
        if (stargate$ticks != 0)
            compound.putInt(TAG_STATE, stargate$ticks);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void fromNbt(CompoundTag compound, CallbackInfo ci) {
        this.stargate$ticks = compound.getInt(TAG_STATE);
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