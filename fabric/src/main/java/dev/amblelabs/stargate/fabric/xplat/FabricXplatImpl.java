package dev.amblelabs.stargate.fabric.xplat;

import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.amblelabs.stargate.xplat.IXplatTags;
import dev.amblelabs.stargate.xplat.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.stream.Stream;

public class FabricXplatImpl implements IXplatAbstractions {

    @Override
    public Platform platform() {
        return Platform.FABRIC;
    }

    @Override
    public boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isModPresent(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    @Override
    public void initPlatformSpecific() {

    }

    @Override
    public void sendPacketToPlayer(ServerPlayer target, CustomPacketPayload packet) {
        ServerPlayNetworking.send(target, packet);
    }

    @Override
    public void sendPacketToAll(Stream<ServerPlayer> targets, CustomPacketPayload packet) {
        Packet<?> pkt = this.toVanilla(packet);
        targets.forEach(player -> player.connection.send(pkt));
    }

    @Override
    public void sendPacketNear(Vec3 pos, double radius, ServerLevel dimension, CustomPacketPayload packet) {
        sendPacketToAll(PlayerLookup.around(dimension, pos, radius).stream(), packet);
    }

    @Override
    public void sendPacketTracking(Entity entity, CustomPacketPayload packet) {
        sendPacketToAll(PlayerLookup.tracking(entity).stream(), packet);
    }

    @Override
    public Packet<ClientCommonPacketListener> toVanilla(CustomPacketPayload message) {
        return ServerPlayNetworking.createS2CPacket(message);
    }

    private static final IXplatTags TAGS = new IXplatTags() {

    };

    @Override
    public IXplatTags tags() {
        return TAGS;
    }

    @Override
    public boolean isDev() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}