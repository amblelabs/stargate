package dev.amblelabs.stargate.client.lib;

import dev.amblelabs.stargate.client.api.mod.network.StargateS2CPacket;
import dev.amblelabs.stargate.common.lib.StargatePackets;
import dev.amblelabs.stargate.common.network.StargateSyncS2CPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class StargateClientPackets {

    public static void registerPackets(Consumer<S2C<?>> r) {
        for (var e : PACKETS) {
            r.accept(e);
        }
    }

    private static final List<S2C<?>> PACKETS = new LinkedList<>();

    static {
        s2c(StargatePackets.STARGATE_SYNC, StargateSyncS2CPayload.class);
    }

    private static <T extends CustomPacketPayload & StargateS2CPacket> void s2c(CustomPacketPayload.Type<T> id, Class<T> clazz) {
        PACKETS.add(new S2C<>(id, clazz));
    }

    public record S2C<T extends CustomPacketPayload & StargateS2CPacket>(CustomPacketPayload.Type<T> type, Class<T> clazz) implements StargatePackets.Entry<T> {

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, T> codec() {
            throw new RuntimeException(new IllegalAccessException());
        }
    }
}
