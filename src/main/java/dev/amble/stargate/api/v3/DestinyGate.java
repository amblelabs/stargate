package dev.amble.stargate.api.v3;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v3.state.stargate.client.ClientDestinyState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DestinyGate extends Stargate {

    public static Identifier ID = StargateMod.id("destiny");

    public DestinyGate(ServerWorld world, BlockPos pos, Direction direction) {
        super(world, pos, direction);
    }

    public DestinyGate(NbtCompound nbt, boolean isClient) {
        super(nbt, isClient);
    }

    @Override
    protected void attachClientState() {
        this.addState(new ClientDestinyState());
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
