package dev.amblelabs.lib.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;

public abstract class FabricAmbleModelProvider extends FabricModelProvider {

    public FabricAmbleModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {

    }
}
