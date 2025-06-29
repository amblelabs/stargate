package dev.amble.stargate.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StargateDataCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("stargate")
                .then(literal("dump").then(argument("address", StringArgumentType.string()).executes(context -> {
                    String address = StringArgumentType.getString(context, "address");
                    NbtCompound nbt = ServerStargateNetwork.get().get(address).toNbt(true);

                    context.getSource().sendFeedback(() -> NbtHelper.toPrettyPrintedText(nbt), false);
                    return Command.SINGLE_SUCCESS;
                }))));
    }
}
