package dev.amblelabs.stargate.api.mod.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface StargateC2SPacket extends CustomPacketPayload {
    void handle(MinecraftServer server, ServerPlayer player);
}
