package dev.amble.stargate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.behavior.GateManagerBehavior;
import dev.amble.stargate.api.v3.state.GateState;
import dev.amble.stargate.command.argumenttypes.StargateArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StargateDialCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("stargate").then(literal("dial").then(argument("from", StargateArgumentType.server()).then(argument("to", StargateArgumentType.server())
                .executes(context -> execute(context, false))
                .then(argument("force", BoolArgumentType.bool())
                        .executes(ctx -> execute(ctx, true)))
        ))));
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, boolean tryForce) throws CommandSyntaxException {
        boolean force = tryForce && BoolArgumentType.getBool(ctx, "force");

        Stargate stargate = StargateArgumentType.getGate(ctx, "from").get(ctx);
        Stargate targetGate = StargateArgumentType.getGate(ctx, "to").get(ctx);

        if (stargate == null || targetGate == null) {
            ctx.getSource().sendFeedback(() -> Text.translatable("command.stargate.generic.missing"), false);
            return 0;
        }

        closeIfForce(ctx, stargate, force);
        closeIfForce(ctx, targetGate, force);

        GateState.Closed closed = stargate.stateOrNull(GateState.Closed.state);

        if (closed == null) {
            ctx.getSource().sendFeedback(() -> Text.translatable("command.stargate.generic.unavailable"), false);
            return 0;
        }

        //closed.address = targetGate.address().text();

        stargate.markDirty();
        targetGate.markDirty();
        return 1;
    }

    private static void closeIfForce(CommandContext<ServerCommandSource> ctx, Stargate stargate, boolean force) {
        if (force && stargate.stateOrNull(GateState.Closed.state) == null) {
            GateManagerBehavior.INSTANCE.set(stargate, new GateState.Closed());
        }
    }
}
