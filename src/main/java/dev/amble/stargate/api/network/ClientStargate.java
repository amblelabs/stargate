package dev.amble.stargate.api.network;

import dev.amble.stargate.api.kernels.GateState;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.client.util.ShakeUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ClientStargate extends Stargate {

    private boolean aged;

    public ClientStargate(NbtCompound nbt) {
        super(nbt);
    }

    @Override
    public void age() {
        this.aged = true;
    }

    @Override
    public boolean isAged() {
        return aged;
    }

    @Override
    public void tick() {
        super.tick();

        if (!(this.state() instanceof GateState.PreOpen))
            return;

        BlockPos pos = this.address().pos().getPos();

        if (!MinecraftClient.getInstance().world.isChunkLoaded(pos))
            return;

        ShakeUtil.shakeFromGate(this);
    }

    @Override
    public void dispose() {
        ClientStargateNetwork.get().remove(this.address());
    }
}
