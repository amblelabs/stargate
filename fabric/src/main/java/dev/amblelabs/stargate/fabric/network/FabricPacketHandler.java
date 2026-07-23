package dev.amblelabs.stargate.fabric.network;

import dev.amblelabs.stargate.api.mod.network.StargateC2SPacket;
import dev.amblelabs.stargate.client.api.mod.network.StargateS2CPacket;
import dev.amblelabs.stargate.client.lib.StargateClientPackets;
import dev.amblelabs.stargate.common.lib.StargatePackets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class FabricPacketHandler {

    @SuppressWarnings("EmptyMethod")
    public static void init() {
        StargatePackets.registerPackets(FabricPacketHandler::registerCommon);
    }

    @SuppressWarnings("EmptyMethod")
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        StargateClientPackets.registerPackets(FabricPacketHandler::registerClient);
    }

    private static <T extends CustomPacketPayload> void registerCommon(StargatePackets.Entry<T> entry) {
        PayloadTypeRegistry.playC2S().register(entry.type(), entry.codec());
        if (entry instanceof StargatePackets.C2S<?> packet) registerC2S(packet);
    }

    private static <T extends CustomPacketPayload & StargateC2SPacket> void registerC2S(StargatePackets.C2S<T> packet) {
        ServerPlayNetworking.registerGlobalReceiver(packet.type(), (payload, context)
                -> context.server().execute(() -> payload.handle(context.server(), context.player())));
    }

    @Environment(EnvType.CLIENT)
    private static <T extends CustomPacketPayload & StargateS2CPacket> void registerClient(StargateClientPackets.S2C<T> packet) {
        ClientPlayNetworking.registerGlobalReceiver(packet.type(), (payload, context) -> payload.handle(context.client(), context.player()));
    }
}
