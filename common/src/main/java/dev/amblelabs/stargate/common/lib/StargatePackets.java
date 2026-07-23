package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.mod.network.StargateC2SPacket;
import dev.amblelabs.stargate.common.network.StargateSyncS2CPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class StargatePackets {

    public static void registerPackets(Consumer<Entry<?>> r) {
        for (var e : PACKETS.entrySet()) {
            r.accept(e.getValue());
        }
    }

    private static final Map<ResourceLocation, Entry<?>> PACKETS = new LinkedHashMap<>();

    public static final CustomPacketPayload.Type<StargateSyncS2CPayload> STARGATE_SYNC = s2c("stargate/sync", StargateSyncS2CPayload.CODEC);

    private static <T extends CustomPacketPayload & StargateC2SPacket> CustomPacketPayload.Type<T> c2s(String name, Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        ResourceLocation loc = StargateAPI.modLoc(name);
        var id = new CustomPacketPayload.Type<T>(loc);
        var old = PACKETS.put(loc, new C2S<>(id, clazz, codec));

        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return id;
    }

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> s2c(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        ResourceLocation loc = StargateAPI.modLoc(name);
        var id = new CustomPacketPayload.Type<T>(loc);
        var old = PACKETS.put(loc, new S2C<>(id, codec));

        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return id;
    }

    public interface Entry<T extends CustomPacketPayload> {
        CustomPacketPayload.Type<T> type();
        StreamCodec<? super RegistryFriendlyByteBuf, T> codec();
    }

    public record C2S<T extends CustomPacketPayload & StargateC2SPacket>(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) implements Entry<T> {

    }

    public record S2C<T extends CustomPacketPayload>(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) implements Entry<T> {

    }
}
