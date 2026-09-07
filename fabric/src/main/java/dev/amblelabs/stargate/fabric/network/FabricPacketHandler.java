package dev.amblelabs.stargate.fabric.network;

import dev.amblelabs.stargate.common.network.MsgStargateSyncS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class FabricPacketHandler {

    public static void initPackets() {
        PayloadTypeRegistry.playS2C().register(MsgStargateSyncS2C.TYPE, MsgStargateSyncS2C.STREAM_CODEC);
    }

    @SuppressWarnings("EmptyMethod")
    public static void init() {

    }

    @SuppressWarnings("EmptyMethod")
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(MsgStargateSyncS2C.TYPE, makeClientBoundHandler(MsgStargateSyncS2C::handle));
    }

    private static <T extends CustomPacketPayload> ClientPlayNetworking.PlayPayloadHandler<T> makeClientBoundHandler(Consumer<T> handler) {
        return (payload, context) -> handler.accept(payload);
    }

    private static <T extends CustomPacketPayload> ServerPlayNetworking.PlayPayloadHandler<T> makeServerBoundHandler(
            TriConsumer<T, MinecraftServer, ServerPlayer> handle) {
        return (payload, context) -> handle.accept(payload, context.server(), context.player());
    }
}
