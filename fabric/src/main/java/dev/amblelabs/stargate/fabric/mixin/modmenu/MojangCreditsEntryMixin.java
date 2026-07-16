package dev.amblelabs.stargate.fabric.mixin.modmenu;

import com.terraformersmc.modmenu.gui.ModsScreen;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.screens.CreditsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.terraformersmc.modmenu.gui.widget.DescriptionListWidget$MojangCreditsEntry")
public class MojangCreditsEntryMixin {

    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    public void mouseClicked(Minecraft instance, Screen guiScreen) {
        if (instance.screen instanceof ModsScreen mods && StargateAPI.MOD_ID.equals(mods.getSelectedEntry().getMod().getId()))
            guiScreen = CreditsScreen.createAndClose();

        instance.setScreen(guiScreen);
    }
}
