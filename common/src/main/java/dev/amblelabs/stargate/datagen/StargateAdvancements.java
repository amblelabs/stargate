package dev.amblelabs.stargate.datagen;

import dev.amblelabs.lib.datagen.AmbleAdvancementSubProvider;
import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;

import java.util.function.Consumer;

public class StargateAdvancements extends AmbleAdvancementSubProvider {

    public StargateAdvancements() {
        super(StargateAPI.MOD_ID);
    }

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) { }
}