package dev.amblelabs.stargate.neoforge.xplat;

import com.google.common.base.Suppliers;
import dev.amblelabs.stargate.api.ecs.Prototype;
import dev.amblelabs.stargate.common.lib.StargateRegistries;
import dev.amblelabs.stargate.xplat.Platform;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import dev.amblelabs.stargate.xplat.XplatTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NeoForgeXplatImpl implements XplatAbstractions {

    private static Supplier<Registry<Prototype>> PROTOTYPE_REGISTRY = () -> {
        throw new IllegalStateException("Asked for the registry too early!");
    };

    @Override
    public Platform platform() {
        return Platform.FORGE;
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    @Override
    public boolean isModPresent(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public void initPlatformSpecific() {
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent ev) -> {
            final MinecraftServer server = ev.getServer();
            PROTOTYPE_REGISTRY = Suppliers.memoize(() -> server.registryAccess().registryOrThrow(StargateRegistries.PROTOTYPE));
        });
    }

    @Override
    public void sendPacketToPlayer(ServerPlayer target, CustomPacketPayload packet) {
        PacketDistributor.sendToPlayer(target, packet);
    }

    @Override
    public void sendPacketToAll(Stream<ServerPlayer> targets, CustomPacketPayload packet) {
        Packet<?> pkt = packet.toVanillaClientbound();
        targets.forEach(player -> player.connection.send(pkt));
    }

    @Override
    public void sendPacketNear(Vec3 pos, double radius, ServerLevel dimension, CustomPacketPayload packet) {
        PacketDistributor.sendToPlayersNear(dimension, null, pos.x, pos.y, pos.z, radius, packet);
    }

    @Override
    public void sendPacketTracking(Entity entity, CustomPacketPayload packet) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, packet);
    }

    @Override
    @SuppressWarnings({"DataFlowIssue"})
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        return BlockEntityType.Builder.of(func::apply, blocks).build(null);
    }

    @Override
    public Registry<Prototype> getPrototypeRegistry() {
        return PROTOTYPE_REGISTRY.get();
    }

    @Override
    public <B> XplatRegistrar<B> createRegister(ResourceKey<Registry<B>> registryKey) {
        return new NeoForgeRegistrar<>(registryKey);
    }

    @Override
    public <B> XplatRegistrar<B> createRegister(Registry<B> registry) {
        return new NeoForgeRegistrar<>(registry);
    }

    private static final XplatTags TAGS = new XplatTags() {

    };

    @Override
    public XplatTags tags() {
        return TAGS;
    }

    @Override
    public boolean isDev() {
        return !FMLEnvironment.production;
    }
}