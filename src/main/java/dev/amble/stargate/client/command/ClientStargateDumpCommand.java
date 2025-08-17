package dev.amble.stargate.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientStargateDumpCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("stargate-client")
                .then(literal("dump").executes(context -> {
                    NbtCompound nbt = ServerStargateNetwork.get().toNbt(true);
                    context.getSource().sendFeedback(NbtHelper.toPrettyPrintedText(nbt));
                    return Command.SINGLE_SUCCESS;
                })));
    }
}
