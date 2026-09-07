package dev.amblelabs.stargate.fabric.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.amblelabs.lib.api.mod.AmblekitTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

    @Shadow
    private BlockPos destroyBlockPos;
    @Unique
    private BlockPos amblekit$lastUnbreakableBreak;

    @Inject(method = "startDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V"), cancellable = true)
    public void startDestroyBlock(BlockPos loc, Direction face, CallbackInfoReturnable<Boolean> cir, @Local(name = "blockState") BlockState blockState) {
        this.handle(loc, blockState, cir);
    }

    @Inject(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V"), cancellable = true)
    public void continueDestroyBlock(BlockPos posBlock, Direction directionFacing, CallbackInfoReturnable<Boolean> cir, @Local(name = "blockState") BlockState blockState) {
        this.handle(posBlock, blockState, cir);
    }

    @Unique
    private void handle(BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(AmblekitTags.Blocks.UNBREAKABLE)) {
            if (this.minecraft.player.isCrouching() && pos.equals(amblekit$lastUnbreakableBreak)) {
                this.amblekit$lastUnbreakableBreak = null;
                return;
            }

            this.amblekit$lastUnbreakableBreak = pos;
            this.minecraft.player.displayClientMessage(Component.translatable("text.amblekit.unbreakable_confirm"), true);

            cir.setReturnValue(false);
            return;
        }

        this.amblekit$lastUnbreakableBreak = null;
    }
}
