package dev.amblelabs.stargate.fabric.mixin.modmenu;

import dev.amblelabs.stargate.api.StargateAPI;
import org.spongepowered.asm.mixin.Mixin;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import com.terraformersmc.modmenu.util.mod.Mod;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DescriptionListWidget.class)
public class DescriptionListWidgetMixin {

    @Redirect(method = "renderListItems", at = @At(value = "INVOKE", target = "Lcom/terraformersmc/modmenu/util/mod/Mod;getId()Ljava/lang/String;"))
    public String renderList(Mod instance) {
        return StargateAPI.MOD_ID.equals(instance.getId()) ? "minecraft" : instance.getId(); // this neat little trick replaces credits section with "View Credits"
    }
}
