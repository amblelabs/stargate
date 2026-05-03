package dev.amblelabs.stargate.fabric.client;

import dev.amblelabs.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class RegisterClientStuff {

    public static void init() {
//        var x = IClientXplatAbstractions.INSTANCE;
//        x.registerEntityRenderer(AitEntities.FALLING_TARDIS_BLOCK, FallingTardisBlockRenderer::new);
    }

    public static void registerBlockEntityRenderers(@NotNull BlockEntityRendererRegisterer registerer) {
        registerer.registerBlockEntityRenderer(StargateBlockEntities.STARGATE, StargateBlockEntityRenderer::new);
//        registerer.registerBlockEntityRenderer(AitBlockEntities.DOOR_BLOCK_ENTITY, DoorBlockEntityRenderer::new);
//        registerer.registerBlockEntityRenderer(AitBlockEntities.CONSOLE_BLOCK_ENTITY, ConsoleBlockEntityRenderer::new);
    }

    @FunctionalInterface
    public interface BlockEntityRendererRegisterer {
        <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type,
            BlockEntityRendererProvider<? super T> provider);
    }
}