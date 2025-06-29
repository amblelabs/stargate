package dev.amble.stargate.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.ServerStargate;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StargateSyncCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("stargate").then(literal("sync").then(argument("address", StringArgumentType.string()).executes(context -> {
            String address = StringArgumentType.getString(context, "address");

            ServerStargateNetwork network = ServerStargateNetwork.get();
            network.syncPartial((ServerStargate) network.get(address));

            return Command.SINGLE_SUCCESS;
        }))));
    }
}
