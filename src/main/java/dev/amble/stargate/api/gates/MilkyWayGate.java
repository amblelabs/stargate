package dev.amble.stargate.api.gates;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.gates.state.stargate.client.ClientMilkyWayState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MilkyWayGate extends Stargate {

    public static Identifier ID = StargateMod.id("milky_way");

    public MilkyWayGate(ServerWorld world, BlockPos pos, Direction direction) {
        super(world, pos, direction);
    }

    public MilkyWayGate(NbtCompound nbt, boolean isClient) {
        super(nbt, isClient);
    }

    @Override
    protected void attachClientState() {
        this.addState(new ClientMilkyWayState());
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
