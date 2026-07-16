package dev.amblelabs.stargate.common.network;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.api.stargate.ClientStargateNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record StargateSyncS2CPayload(CompoundTag tag) implements CustomPacketPayload {

    public static final ResourceLocation PAYLOAD_ID = StargateAPI.modLoc("stargate/sync");
    public static final CustomPacketPayload.Type<StargateSyncS2CPayload> ID = new CustomPacketPayload.Type<>(PAYLOAD_ID);

    public static final StreamCodec<FriendlyByteBuf, StargateSyncS2CPayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.TRUSTED_COMPOUND_TAG, StargateSyncS2CPayload::tag, StargateSyncS2CPayload::new);

    @Environment(EnvType.CLIENT)
    public void handle(Minecraft minecraft, LocalPlayer player) {
        if (minecraft.level != null)
            ClientStargateNetwork.get(minecraft.level).upsert(tag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
