package dev.amble.stargate.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientStargateDumpCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("stargate-client")
                .then(literal("dump").executes(context -> {
                    //NbtCompound nbt = StargateClientData.get().toNbt(true);
                    //context.getSource().sendFeedback(NbtHelper.toPrettyPrintedText(nbt));
                    return Command.SINGLE_SUCCESS;
                })));
    }
}
