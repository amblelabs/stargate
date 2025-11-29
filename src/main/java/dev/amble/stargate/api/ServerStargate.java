package dev.amble.stargate.api;

import dev.amble.stargate.api.data.StargateServerData;
import dev.amble.stargate.api.event.address.StargateRemoveEvent;
import dev.amble.stargate.api.event.init.StargateCreatedEvents;
import dev.amble.stargate.api.state.stargate.GateIdentityState;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServerStargate extends Stargate {

    public ServerStargate(GateIdentityState identity, ServerWorld world, BlockPos pos, Direction direction) {
        super(identity, world, pos, direction);
        StargateCreatedEvents.handleCreation(this);
    }

    public ServerStargate(GateIdentityState identity, NbtCompound nbt) {
        super(identity, nbt, false);
    }

    @Override
    public @NotNull ServerStargate asGate() {
        return (ServerStargate) super.asGate();
    }

    @Override
    public @NotNull ServerWorld world() {
        return (ServerWorld) Objects.requireNonNull(super.world());
    }

    public void remove() {
        ServerWorld world = this.world();

        this.kernel().shape.destroy(world, pos, facing);
        TEvents.handle(new StargateRemoveEvent(StargateServerData.get(world), this));
    }
}
