package dev.amblelabs.stargate.fabric.mixin;

import dev.amblelabs.stargate.common.blocks.behavior.StargateStrippable;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// https://github.com/VazkiiMods/Botania/blob/1.18.x/Fabric/src/main/java/vazkii/botania/fabric/mixin/FabricMixinAxeItem.java
@Mixin(AxeItem.class)
public class FabricAxeItemMixin {

    @Inject(method = "getStripped", at = @At("RETURN"), cancellable = true)
    private void stripBlock(BlockState unstrippedState, CallbackInfoReturnable<Optional<BlockState>> cir) {
        if (cir.getReturnValue().isEmpty()) {
            var block = StargateStrippable.STRIPPABLE.get(unstrippedState.getBlock());

            if (block != null) {
                cir.setReturnValue(Optional.of(block.withPropertiesOf(unstrippedState)));
            }
        }
    }
}