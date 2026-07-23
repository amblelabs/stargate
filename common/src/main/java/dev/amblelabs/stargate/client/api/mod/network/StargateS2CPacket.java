package dev.amblelabs.stargate.client.api.mod.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface StargateS2CPacket extends CustomPacketPayload {
    @Environment(EnvType.CLIENT)
    void handle(Minecraft minecraft, LocalPlayer player);
}
