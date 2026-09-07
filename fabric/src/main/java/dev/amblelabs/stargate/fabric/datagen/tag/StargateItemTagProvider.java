package dev.amblelabs.stargate.fabric.datagen.tag;

import dev.amblelabs.lib.fabric.datagen.FabricAmbleTagProvider;
import dev.amblelabs.stargate.xplat.XplatTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class StargateItemTagProvider extends FabricAmbleTagProvider.ItemTagProvider {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final XplatTags xtags;

    public StargateItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BlockTagProvider blockTagProvider, XplatTags xtags) {
        super(output, completableFuture, blockTagProvider);

        this.xtags = xtags;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}