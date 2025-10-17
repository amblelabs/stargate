package dev.amble.stargate.api.data;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.gates.Stargate;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.function.Supplier;

public class StargateRef implements StargateLinkable {

    private final Supplier<World> world;

    protected long address = -1L;
    protected @Nullable WeakReference<Stargate> stargate;

    public StargateRef(Supplier<World> world) {
        this.world = world;
    }

    @Override
    public @Nullable Stargate asGate() {
        return stargate != null ? stargate.get() : address != -1
                ? StargateData.apply(world.get(), data -> data.getById(address)) : null;
    }

    @Override
    public boolean link(@Nullable Stargate gate) {
        Stargate oldGate = this.stargate != null ? this.stargate.get() : null;

        this.stargate = gate != null ? new WeakReference<>(gate) : null;
        this.address = gate != null ? StargateServerData.getAnyId(gate) : -1;

        if (world.get() instanceof ServerWorld serverWorld) {
            StargateServerData data = StargateServerData.getOrCreate(serverWorld);

            if (oldGate != null)
                data.unmark(oldGate);

            if (this.isLinked())
                data.mark(this);
        };

        return this.isLinked();
    }

    @Override
    public boolean link(long address) {
        Stargate stargate = StargateData.apply(world.get(), data -> data.getById(address));
        return this.link(stargate);
    }

    @Override
    public void unlink() {
        link(null);
    }

    public void readNbt(NbtCompound nbt) {
        if (!nbt.contains("Stargate")) return;
        this.link(nbt.getLong("Stargate"));
    }

    public void writeNbt(NbtCompound nbt) {
        if (!this.isLinked()) return ;

        nbt.putLong("Stargate", this.address);
    }

    public long address() {
        return address;
    }

    public static @Nullable Stargate resolveGlobal(long globalAddress, boolean isClient) {
        if (isClient)
            return StargateClientData.get().getGlobal(globalAddress);

        RegistryKey<World> key = AddressProvider.Global.getTarget(globalAddress);
        ServerWorld world = ServerLifecycleHooks.get().getWorld(key);

        if (world != null)
            return StargateServerData.apply(world, data -> data.getGlobal(globalAddress));

        return null;
    }
}
