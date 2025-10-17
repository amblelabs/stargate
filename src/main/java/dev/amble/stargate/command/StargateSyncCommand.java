package dev.amble.stargate.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.command.argumenttypes.StargateArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StargateSyncCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("stargate").then(literal("sync").then(argument("gate", StargateArgumentType.server()).executes(context -> {
            Stargate gate = StargateArgumentType.getGate(context, "gate").get(context);

            //ServerStargateNetwork network = ServerStargateNetwork.get();
            //network.syncPartial(gate);

            return Command.SINGLE_SUCCESS;
        }))));
    }
}
