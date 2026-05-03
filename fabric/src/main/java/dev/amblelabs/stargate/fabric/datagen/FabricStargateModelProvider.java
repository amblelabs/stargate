package dev.amblelabs.stargate.fabric.datagen;

import dev.amblelabs.lib.fabric.datagen.FabricAmbleModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;

public class FabricStargateModelProvider extends FabricAmbleModelProvider {

    public FabricStargateModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        // will use later
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        // will use later
    }
}
