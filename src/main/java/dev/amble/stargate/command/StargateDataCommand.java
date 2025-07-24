package dev.amble.stargate.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import dev.amble.stargate.api.network.ServerStargate;
import dev.amble.stargate.command.argumenttypes.StargateArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StargateDataCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("stargate")
                .then(literal("data").then(argument("gate", StargateArgumentType.server()).executes(context -> {
                    ServerStargate gate = StargateArgumentType.getGate(context, "gate").get(context);
                    NbtCompound nbt = gate.toNbt(true);

                    context.getSource().sendFeedback(() -> NbtHelper.toPrettyPrintedText(nbt), false);
                    return Command.SINGLE_SUCCESS;
                }))));
    }
}
