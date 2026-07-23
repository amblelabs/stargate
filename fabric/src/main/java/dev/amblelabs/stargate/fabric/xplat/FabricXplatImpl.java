package dev.amblelabs.stargate.fabric.xplat;

import com.google.common.base.Suppliers;
import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.common.lib.StargateRegistries;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.amblelabs.stargate.xplat.IXplatTags;
import dev.amblelabs.stargate.xplat.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FabricXplatImpl implements IXplatAbstractions {

    private static Supplier<Registry<PrototypeRegistryEntry>> PROTOTYPE_REGISTRY = Suppliers.memoize(() -> {
        throw new IllegalStateException("Asked for the registry too early!");
    });

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
        DynamicRegistries.registerSynced(StargateRegistries.PROTOTYPE, PrototypeRegistryEntry.CODEC);

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            PROTOTYPE_REGISTRY = Suppliers.memoize(() -> server.registryAccess().registryOrThrow(StargateRegistries.PROTOTYPE));
        });
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

    @Override
    @SuppressWarnings("deprecation")
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(func::apply, blocks).build();
    }

    @Override
    public Registry<PrototypeRegistryEntry> getPrototypeRegistry() {
        return PROTOTYPE_REGISTRY.get();
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