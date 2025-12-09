package dev.amble.stargate.mixins;

import dev.amble.stargate.init.StargateItems;
import dev.amble.stargate.util.ExBoatType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {

    @Shadow public abstract BoatEntity.Type getVariant();

    @Inject(method = "asItem", at = @At("HEAD"), cancellable = true)
    public void asItem(CallbackInfoReturnable<Item> cir) {
        if (this.getVariant() == ExBoatType.NAQUADAH) cir.setReturnValue(StargateItems.NAQUADAH_BOAT);
    }
}
