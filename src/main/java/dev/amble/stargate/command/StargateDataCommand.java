package dev.amble.stargate.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.data.StargateRef;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StargateDataCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("stargate")
                .then(literal("data").then(argument("global_address", LongArgumentType.longArg(0)).executes(context -> {
                    long address = LongArgumentType.getLong(context, "global_address");

                    Stargate gate = StargateRef.resolveGlobal(address, false);
                    if (gate == null) return -1;

                    NbtCompound nbt = new NbtCompound();
                    gate.toNbt(nbt, false);

                    context.getSource().sendFeedback(() -> NbtHelper.toPrettyPrintedText(nbt), false);
                    return Command.SINGLE_SUCCESS;
                }))));
    }
}
