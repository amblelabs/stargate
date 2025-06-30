package dev.amble.stargate.core.fluid;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.init.StargateItems;
import dev.amble.stargate.item.EmptyContainerItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class StargateFluids {

    public static FlowableFluid STILL_LIQUID_NAQUADAH;
    public static FlowableFluid FLOWING_LIQUID_NAQUADAH;
    public static Block LIQUID_NAQUADAH_BLOCK;
    public static Item LIQUID_NAQUADAH;

    public static void register() {
        STILL_LIQUID_NAQUADAH = Registry.register(
                Registries.FLUID,
                new Identifier(StargateMod.MOD_ID, "still_liquid_naquadah"),
                new LiquidNaquadahFluid.Still()
        );
        FLOWING_LIQUID_NAQUADAH = Registry.register(
                Registries.FLUID,
                new Identifier(StargateMod.MOD_ID, "flowing_liquid_naquadah"),
                new LiquidNaquadahFluid.Flowing()
        );

        LIQUID_NAQUADAH_BLOCK = Registry.register(
                Registries.BLOCK,
                new Identifier(StargateMod.MOD_ID, "liquid_naquadah_block"),
                new LiquidNaquadahFluidBlock(
                        STILL_LIQUID_NAQUADAH,
                        FabricBlockSettings.copyOf(Blocks.WATER)
                )
        );

        LIQUID_NAQUADAH = Registry.register(
                Registries.ITEM,
                new Identifier(StargateMod.MOD_ID, "liquid_naquadah"),
                new EmptyContainerItem(
                        STILL_LIQUID_NAQUADAH,
                        new FabricItemSettings().recipeRemainder(StargateItems.EMPTY_CONTAINER).maxCount(1)
                )
        );
    }
}