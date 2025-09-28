package dev.amble.stargate.api.v3;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Addressable;
import dev.amble.stargate.api.kernels.GateShape;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.api.v3.event.StargateCreatedEvent;
import dev.amble.stargate.api.v3.event.StargateTickEvent;
import dev.amble.stargate.api.v3.event.state.StateAddedEvent;
import dev.amble.stargate.api.v3.event.state.StateRemovedEvent;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.client.ClientGenericGateState;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import dev.drtheo.yaar.state.TStateContainer;
import dev.drtheo.yaar.state.TStateRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public abstract class Stargate extends TStateContainer.Delegate implements Addressable, NbtSerializer {

    private final Address address;
    private final boolean isClient;

    private TState.Type<?> curState;
    private boolean dirty;

    public Stargate(DirectedGlobalPos pos) {
        super(TStateRegistry.createArrayHolder());

        this.address = new Address(pos);
        this.isClient = false;

        TState<?> state = this.createDefaultState();

        this.addState(state);
        this.curState = state.type();

        this.attachState(true, false);

        TEvents.handle(new StargateCreatedEvent(this));
    }

    public static Stargate fromNbt(NbtCompound nbt, boolean isClient) {
        Identifier id = new Identifier(nbt.getString("Id"));
        return GateKernelRegistry.get().get(id).loader().load(nbt, isClient);
    }

    public Stargate(NbtCompound nbt, boolean isClient) {
        super(TStateRegistry.createArrayHolder());

        this.address = Address.fromNbt(nbt.getCompound("Address"));
        this.isClient = isClient;

        this.updateStates(nbt, isClient);
        this.attachState(false, isClient);
    }

    public void updateStates(NbtCompound nbt, boolean isClient) {
        NbtCompound states = nbt.getCompound("States");

        for (String key : states.getKeys()) {
            if (TStateRegistry.get(new Identifier(key)) instanceof TState.NbtBacked<?> serializable) {
                NbtElement state = states.get(key);

                if (state instanceof NbtCompound compound) {
                    this.addState(serializable.decode(compound, isClient));
                } else {
                    this.removeState(serializable);
                }
            }
        }

        Identifier prevStateId = new Identifier(nbt.getString("prevState"));
        TState.Type<?> type = TStateRegistry.get(prevStateId);

        if (type == null || this.stateOrNull(type) == null) {
            StargateMod.LOGGER.warn("Bad state: '{}', fixing", prevStateId);
            TState<?> state = this.createDefaultState();

            this.addState(state);
            this.curState = state.type();
            return;
        }

        this.curState = type;
    }

    protected void attachState(boolean created, boolean isClient) {
        if (isClient) {
            this.attachClientState();
        }
    }

    @Environment(EnvType.CLIENT)
    protected void attachClientState() {
        this.addState(new ClientGenericGateState(false));
    }

    public void tick() {
        TEvents.handle(new StargateTickEvent(this));
    }

    public @Nullable TState.Type<?> currentStateType() {
        return curState;
    }

    public @Nullable BasicGateStates<?> getCurrentState() {
        return (BasicGateStates<?>) stateOrNull(curState);
    }

    public void setCurState(TState.Type<?> state) {
        this.curState = state;
    }

    @Override
    @Contract(mutates = "this")
    public boolean addState(@NotNull TState<?> state) {
        boolean result = super.addState(state);

        if (result)
            TEvents.handle(new StateAddedEvent(this, state));

        return result;
    }

    @Override
    @Contract(mutates = "this")
    public <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type) {
        T result = super.removeState(type);

        if (result != null)
            TEvents.handle(new StateRemovedEvent(this, result));

        return result;
    }

    public abstract GateShape shape();

    @Override
    public Address address() {
        return address;
    }

    public void doHere(BiConsumer<World, BlockPos> consumer) {
        DirectedGlobalPos pos = this.address.pos();
        ServerWorld world = ServerLifecycleHooks.get().getWorld(pos.getDimension());

        if (world != null)
            consumer.accept(world, pos.getPos());
    }

    public void playSound(SoundEvent event) {
        this.doHere((world, blockPos) -> world.playSound(null, blockPos, event, SoundCategory.BLOCKS));
    }

    public boolean isClient() {
        return isClient;
    }

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
        nbt.putString("Id", this.id().toString());
        nbt.put("Address", address.toNbt());

        NbtCompound states = new NbtCompound();
        this.forEachState((i, state) -> stateToNbt(states, i, state, isClient));

        nbt.put("States", states);
        nbt.putString("prevState", this.curState.id().toString());
    }

    @SuppressWarnings("rawtypes")
    private <T extends TState<T>> void stateToNbt(NbtCompound nbt, int i, @Nullable TState<T> state, boolean isClient) {
        if (state == null) {
            if (!isClient) // do the diffing only if we're serializing for client
                nbt.put(TStateRegistry.get(i).id().toString(), NbtByte.ZERO);

            return;
        }

        TState.Type<T> type = state.type();

        if (!(type instanceof TState.NbtBacked backed))
            return;

        nbt.put(type.id().toString(), backed.encode(state, isClient));
    }

    public boolean dirty() {
        return dirty;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void unmarkDirty() {
        this.dirty = false;
    }

    protected abstract TState<?> createDefaultState();

    public abstract Identifier id();

    public void dispose() {
        ServerStargateNetwork.get().remove(this.address);
    }
}
