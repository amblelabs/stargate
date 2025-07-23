package dev.amble.stargate.command.argumenttypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.ServerStargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class StargateArgumentType implements ArgumentType<StargateArgumentType.StargateContext> {

    public static final SimpleCommandExceptionType INVALID_UUID = new SimpleCommandExceptionType(Text.translatable("argument.uuid.invalid"));

    @Override
    public StargateContext parse(StringReader reader) throws CommandSyntaxException {
        if (reader.canRead() && reader.peek() == '^') {
            reader.skip();

            return context -> {
                HitResult hit = context.getSource().getEntity().raycast(16, 0, false);

                if (!(hit instanceof BlockHitResult blockHit))
                    throw INVALID_UUID.create();

                BlockEntity blockEntity = context.getSource().getWorld().getBlockEntity(blockHit.getBlockPos());

                if (!(blockEntity instanceof StargateBlockEntity linkable))
                    throw INVALID_UUID.create();

                return (ServerStargate) linkable.gate().get();
            };
        }

        String address = reader.readString();
        return context -> ServerStargateNetwork.get().get(address);
    }

    public static StargateContext getGate(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, StargateContext.class);
    }

    public static StargateArgumentType server() {
        return new StargateArgumentType();
    }

    @FunctionalInterface
    public interface StargateContext {
        ServerStargate get(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;
    }
}
