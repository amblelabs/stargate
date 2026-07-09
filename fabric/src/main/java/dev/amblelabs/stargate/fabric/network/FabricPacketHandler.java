package dev.amblelabs.stargate.fabric.network;

import dev.amblelabs.stargate.common.network.StargateSyncS2CPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

public class FabricPacketHandler {

    @SuppressWarnings("EmptyMethod")
    public static void init() {
        PayloadTypeRegistry.playS2C().register(StargateSyncS2CPayload.ID, StargateSyncS2CPayload.CODEC);
    }

    @SuppressWarnings("EmptyMethod")
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(StargateSyncS2CPayload.ID, s2c(p -> p::handle));
    }

    @SuppressWarnings("unused") // will be used later
    private static <T extends CustomPacketPayload> ServerPlayNetworking.PlayPayloadHandler<T> c2s(Function<T, C2SPacketHandler> handler) {
        return (payload, context) -> context.server().execute(() -> handler.apply(payload).handle(context.server(), context.player()));
    }

    @SuppressWarnings("unused") // will be used later
    @Environment(EnvType.CLIENT)
    private static <T extends CustomPacketPayload> ClientPlayNetworking.PlayPayloadHandler<T> s2c(Function<T, S2CPacketHandler> handler) {
        return (payload, context) -> handler.apply(payload).handle(context.client(), context.player());
    }

    // TODO: move these to common side
    @FunctionalInterface
    interface C2SPacketHandler {
        void handle(MinecraftServer server, ServerPlayer player);
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    interface S2CPacketHandler {
        void handle(Minecraft minecraft, LocalPlayer player);
    }
}
