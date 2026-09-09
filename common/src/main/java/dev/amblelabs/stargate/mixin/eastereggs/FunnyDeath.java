package dev.amblelabs.stargate.mixin.eastereggs;

import dev.amblelabs.stargate.common.lib.StargateDamageTypes;
import dev.amblelabs.stargate.common.lib.StargateItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CombatTracker.class)
public class FunnyDeath {

    @Shadow
    @Final
    private LivingEntity mob;

    @Inject(method = "getDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getLocalizedDeathMessage(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/network/chat/Component;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void getDeathMessage(CallbackInfoReturnable<Component> cir, CombatEntry combatEntry, DamageSource damageSource) {
        if (damageSource.is(StargateDamageTypes.IRIS) && this.mob instanceof Player player && player.getInventory()
                .contains(stack -> stack.is(StargateItems.GDO.get())))
            cir.setReturnValue(Component.translatable(damageSource.getMsgId() + ".gdo"));
    }
}
