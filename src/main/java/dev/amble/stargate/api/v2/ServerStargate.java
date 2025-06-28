package dev.amble.stargate.api.v2;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ServerStargate extends Stargate {

    public ServerStargate(DirectedGlobalPos pos, GateKernelRegistry.KernelCreator constructor) {
        super(constructor);

        this.kernel.onCreate(pos);
    }

    public ServerStargate(NbtCompound nbt) {
        super(nbt);
    }

    @Override
    public void sync() {
        ServerStargateNetwork.get().syncPartial(this);
    }
}
