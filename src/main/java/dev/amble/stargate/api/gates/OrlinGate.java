package dev.amble.stargate.api.gates;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.gates.state.stargate.OrlinState;
import dev.amble.stargate.api.gates.state.stargate.client.ClientOrlinState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class OrlinGate extends Stargate {

    public static Identifier ID = StargateMod.id("orlin");

    public OrlinGate(ServerWorld world, BlockPos pos, Direction direction) {
        super(world, pos, direction);
    }

    public OrlinGate(NbtCompound nbt, boolean isClient) {
        super(nbt, isClient);
    }

    @Override
    protected void attachIdentity() {
        this.addState(new OrlinState());
    }

    @Override
    protected void attachClientState() {
        this.addState(new ClientOrlinState());
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
