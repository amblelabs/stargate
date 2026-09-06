package dev.amblelabs.stargate.xplat;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.Prototype;
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

import java.util.ServiceLoader;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IXplatAbstractions {

    Platform platform();

    IXplatTags tags();

    boolean isDev();

    boolean isModPresent(String modId);

    boolean isPhysicalClient();

    void initPlatformSpecific();

    void sendPacketToPlayer(ServerPlayer target, CustomPacketPayload packet);

    void sendPacketToAll(Stream<ServerPlayer> targets, CustomPacketPayload packet);

    void sendPacketNear(Vec3 pos, double radius, ServerLevel dimension, CustomPacketPayload packet);

    void sendPacketTracking(Entity entity, CustomPacketPayload packet);

    // https://github.com/VazkiiMods/Botania/blob/13b7bcd9cbb6b1a418b0afe455662d29b46f1a7f/Xplat/src/main/java/vazkii/botania/xplat/IXplatAbstractions.java#L157
    Packet<ClientCommonPacketListener> toVanilla(CustomPacketPayload message);

    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func,
                                                                     Block... blocks);

    Registry<Prototype> getPrototypeRegistry();

    IXplatAbstractions INSTANCE = find();

    private static IXplatAbstractions find() {
        var providers = ServiceLoader.load(IXplatAbstractions.class).stream().toList();

        if (providers.size() != 1) {
            var names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException(
                    "There should be exactly one IXplatAbstractions implementation on the classpath. Found: " + names);
        } else {
            var provider = providers.getFirst();
            StargateAPI.LOGGER.debug("Instantiating xplat impl: {}", provider.type().getName());
            return provider.get();
        }
    }
}
