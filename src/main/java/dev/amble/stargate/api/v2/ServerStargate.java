package dev.amble.stargate.api.v2;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ServerStargate extends Stargate {

    private boolean canSync = false;

    public ServerStargate(DirectedGlobalPos pos, GateKernelRegistry.KernelCreator constructor) {
        super(constructor);

        this.kernel.onCreate(pos);
        this.canSync = true;
    }

    public ServerStargate(NbtCompound nbt) {
        super(nbt);
    }

    @Override
    public void sync() {
        if (!this.canSync) {
            StargateMod.LOGGER.warn("Tried to sync a stargate that was not ready yet!");
            return;
        }
        ServerStargateNetwork.get().syncPartial(this);
    }
}
