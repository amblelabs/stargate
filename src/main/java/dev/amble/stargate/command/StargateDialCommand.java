package dev.amble.stargate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.amble.stargate.api.StargateAccessor;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StargateDialCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("stargate").then(literal("dial").then(argument("address", StringArgumentType.string()).then(argument("target", StringArgumentType.string())
                .executes(context -> execute(context, false))
                .then(argument("force", BoolArgumentType.bool())
                        .executes(ctx -> execute(ctx, true)))
        ))));
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, boolean tryForce) {
        String address = StringArgumentType.getString(ctx, "address");
        String target = StringArgumentType.getString(ctx, "target");
        boolean force = tryForce && BoolArgumentType.getBool(ctx, "force");

        Stargate stargate = ServerStargateNetwork.get().get(address);
        Stargate targetGate = ServerStargateNetwork.get().get(target);

        if (stargate == null || targetGate == null) {
            // TODO: replace with translatable
            ctx.getSource().sendFeedback(() -> Text.literal("No stargate found."), false);
            return 0;
        }

        if (force && !(stargate.state() instanceof GateState.Closed)) {
            if (!(stargate.kernel() instanceof StargateAccessor accessor)) {
                // TODO: replace with translatable
                ctx.getSource().sendFeedback(() -> Text.literal("Stargate doesn't implement an accessor"), false);
                return 0;
            }

            accessor.setState(new GateState.Closed());
        }

        if (!(stargate.state() instanceof GateState.Closed closed) || !stargate.canDialTo(targetGate)) {
            // TODO: replace with translatable
            ctx.getSource().sendFeedback(() -> Text.literal("The gate is not available."), false);
            return 0;
        }

        closed.setAddress(target);
        stargate.markDirty();
        targetGate.markDirty();
        return 1;
    }
}
