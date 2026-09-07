package dev.amblelabs.stargate.fabric.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.amblelabs.lib.api.mod.AmblekitTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: move to amblekit common
@Mixin(MultiPlayerGameMode.class)
public class PreventUnbreakableBlockBreaking {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private BlockPos amblekit$lastUnbreakableBreak;

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;"), cancellable = true)
    public void playerWillDestroy(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(name = "blockState") BlockState blockState) {
        if (blockState.is(AmblekitTags.Blocks.UNBREAKABLE) && !pos.equals(amblekit$lastUnbreakableBreak)) {
            this.amblekit$lastUnbreakableBreak = pos;
            if (this.minecraft.player.isCrouching())
                cir.setReturnValue(false);

            this.minecraft.player.displayClientMessage(Component.translatable("Break the block again while sneaking to confirm."), true);
        }
    }
}
