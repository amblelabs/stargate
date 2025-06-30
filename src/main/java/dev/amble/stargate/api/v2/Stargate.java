package dev.amble.stargate.api.v2;

import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Disposable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public abstract class Stargate implements StargateKernel, Disposable {

    protected final StargateKernel.Impl kernel;

    protected Stargate(GateKernelRegistry.KernelCreator constructor) {
        this.kernel = constructor.create(this);
    }

    public Stargate(NbtCompound nbt) {
        String rawModel = nbt.getString("Model");

        Identifier modelId = rawModel.isEmpty() ? MilkyWayGateKernel.ID
                : Identifier.tryParse(rawModel);

        this.kernel = GateKernelRegistry.get().get(modelId).create(this);
        this.loadNbt(nbt);
    }

    // TODO: impl this
    @Override
    public void dispose() {

    }

    @Override
    public void tick() {
        this.kernel.tick();
    }

    @Override
    public void loadNbt(NbtCompound nbt, boolean isSync) {
        // "Model" is handled in the constructor
        this.kernel.loadNbt(nbt.getCompound("Kernel"), isSync);
    }

    @Override
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("Model", this.kernel.id().toString());
        nbt.put("Kernel", this.kernel.toNbt());

        return nbt;
    }

    @Override
    public Address address() {
        return kernel.address();
    }

    @Override
    public long energy() {
        return kernel.energy();
    }

    @Override
    public long maxEnergy() {
        return kernel.maxEnergy();
    }

    @Override
    public boolean canDialTo(Stargate stargate) {
        return kernel.canDialTo(stargate);
    }

    @Override
    public long energyToDial(Address address) {
        return kernel.energyToDial(address);
    }

    @Override
    public GateShape shape() {
        return kernel.shape();
    }

    @Override
    public GateState state() {
        return kernel.state();
    }

    @Override
    public boolean dirty() {
        return this.kernel.dirty();
    }

    @Override
    public void unmarkDirty() {
        this.kernel.unmarkDirty();
    }

    @Override
    public void markDirty() {
        this.kernel.markDirty();
    }

    public StargateKernel.Impl kernel() {
        return kernel;
    }
}
