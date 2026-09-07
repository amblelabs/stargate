package dev.amblelabs.stargate.common.network;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.api.stargate.ClientStargateNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record MsgStargateSyncS2C(CompoundTag tag) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MsgStargateSyncS2C> TYPE = new Type<>(StargateAPI.modLoc("stargate/sync"));

    public static final StreamCodec<FriendlyByteBuf, MsgStargateSyncS2C> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.TRUSTED_COMPOUND_TAG, MsgStargateSyncS2C::tag, MsgStargateSyncS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle() {
        Handler.handle(this);
    }

    public static final class Handler {

        public static void handle(MsgStargateSyncS2C self) {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.level != null)
                ClientStargateNetwork.get(minecraft.level).upsert(self.tag);
        }
    }
}
