package dev.amble.stargate.api.data;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.service.Services;
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
        if (stargate != null)
            return stargate.get();

        if (address != -1) {
            System.out.println("fallback for A=" + address + "; client? " + world.get().isClient);
            Stargate found = StargateData.apply(world.get(), data -> data.getById(address));

            if (found != null) {
                System.out.println("found!");
                this.stargate = new WeakReference<>(found);
                return found;
            }
        }

        return null;
    }

    private boolean link(long id, @Nullable Stargate gate) {
        System.out.println("Linking: " + id + "/" + gate + ": client? " + world.get().isClient);
        Stargate oldGate = this.asGate();

        this.stargate = gate != null ? new WeakReference<>(gate) : null;
        this.address = id;

        if (oldGate != this.asGate() && world.get() instanceof ServerWorld serverWorld) {
            StargateServerData data = StargateServerData.getOrCreate(serverWorld);

            if (oldGate != null)
                data.unmark(oldGate);

            if (this.isLinked())
                data.mark(this);
        }

        return this.isLinked();
    }

    @Override
    public boolean link(@Nullable Stargate gate) {
        return link(gate != null ? gate.globalId() : -1, gate);
    }

    @Override
    public boolean link(long address) {
        return link(address, null);
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
        if (globalAddress == -1)
            return null;

        if (isClient)
            return Services.GATES.getClient().getGlobal(globalAddress);

        RegistryKey<World> key = AddressProvider.Global.getTarget(globalAddress);
        ServerWorld world = ServerLifecycleHooks.get().getWorld(key);

        if (world != null)
            return StargateServerData.apply(world, data -> data.getGlobal(globalAddress));

        return null;
    }
}
