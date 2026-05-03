package dev.amblelabs.stargate.fabric.datagen.tag;

import dev.amblelabs.lib.fabric.datagen.FabricAmbleItemTagProvider;
import dev.amblelabs.stargate.xplat.IXplatTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class StargateItemTagProvider extends FabricAmbleItemTagProvider {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final IXplatTags xtags;

    public StargateItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BlockTagProvider blockTagProvider, IXplatTags xtags) {
        super(output, completableFuture, blockTagProvider);

        this.xtags = xtags;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}