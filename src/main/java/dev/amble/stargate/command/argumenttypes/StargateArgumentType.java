package dev.amble.stargate.command.argumenttypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.amble.stargate.api.v2.ServerStargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class StargateArgumentType implements ArgumentType<ServerStargate> {
    @Override
    public ServerStargate parse(StringReader reader) throws CommandSyntaxException {
        if (reader.canRead() && reader.peek() == '^') {
            reader.skip();

            /*return context -> {
                HitResult hit = context.getSource().getEntity().raycast(16, 0, false);

                if (!(hit instanceof BlockHitResult blockHit))
                    throw INVALID_UUID.create();

                BlockEntity blockEntity = context.getSource().getWorld().getBlockEntity(blockHit.getBlockPos());

                if (!(blockEntity instanceof StargateBlockEntity linkable))
                    throw INVALID_UUID.create();

                return linkable.gate().get();
            };
        }

        String string = reader.getRemaining();

        Matcher matcher = VALID_CHARACTERS.matcher(string);

        if (!matcher.find())
            throw INVALID_UUID.create();

        String raw = matcher.group(1);

        UUID uuid = UUID.fromString(raw);
        reader.setCursor(reader.getCursor() + raw.length());

        return context -> ServerTardisManager.getInstance().demandTardis(context.getSource().getServer(), uuid);*/
            return null;
        }
        return null;
    }
}
