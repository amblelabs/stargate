package dev.amble.stargate.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.nbt.NbtCompound;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientStargateDataCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("stargate-client")
                .then(literal("data").then(argument("address", StringArgumentType.string()).executes(context -> {
                    String address = StringArgumentType.getString(context, "address");
                    NbtCompound nbt = new NbtCompound();
                    //StargateClientData.get().getGlobal(address).toNbt(nbt, true);
                    //context.getSource().sendFeedback(NbtHelper.toPrettyPrintedText(nbt));
                    return Command.SINGLE_SUCCESS;
                })))
        );
    }
}
