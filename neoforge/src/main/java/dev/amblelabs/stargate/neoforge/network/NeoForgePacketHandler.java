package dev.amblelabs.stargate.neoforge.network;

import dev.amblelabs.stargate.common.network.MsgStargateSyncS2C;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class NeoForgePacketHandler {

    public static void init(IEventBus modBus) {
        modBus.addListener(RegisterPayloadHandlersEvent.class, ev -> {
            final PayloadRegistrar registrar = ev.registrar("0");

            registrar.playToClient(MsgStargateSyncS2C.TYPE, MsgStargateSyncS2C.STREAM_CODEC,
                    makeClientBoundHandler(MsgStargateSyncS2C::handle));
        });
    }

    private static <T extends CustomPacketPayload> IPayloadHandler<T> makeServerBoundHandler(
        TriConsumer<T, MinecraftServer, ServerPlayer> handler) {
        return (m, ctx) -> handler.accept(m, ctx.player().getServer(), (ServerPlayer) ctx.player());
    }

    private static <T extends CustomPacketPayload> IPayloadHandler<T> makeClientBoundHandler(Consumer<T> consumer) {
        return (m, ctx) -> consumer.accept(m);
    }
}