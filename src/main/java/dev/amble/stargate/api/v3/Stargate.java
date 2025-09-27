package dev.amble.stargate.api.v3;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Addressable;
import dev.amble.stargate.api.Disposable;
import dev.amble.stargate.api.kernels.GateShape;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v3.event.StargateCreatedEvent;
import dev.amble.stargate.api.v3.event.StargateTickEvent;
import dev.amble.stargate.api.v3.event.state.StateAddedEvent;
import dev.amble.stargate.api.v3.event.state.StateRemovedEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import dev.drtheo.yaar.state.TStateContainer;
import dev.drtheo.yaar.state.TStateRegistry;
import net.minecraft.nbt.NbtCompound;
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

public abstract class Stargate extends TStateContainer.Delegate implements Addressable, NbtSerializer, Disposable {

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

        TEvents.handle(new StargateCreatedEvent(this));
    }

    public Stargate(NbtCompound nbt, boolean isClient) {
        super(TStateRegistry.createArrayHolder());

        this.address = Address.fromNbt(nbt.getCompound("Address"));
        this.isClient = isClient;

        NbtCompound states = nbt.getCompound("States");

        for (String key : states.getKeys()) {
            if (TStateRegistry.get(new Identifier(key)) instanceof TState.NbtBacked<?> serializable)
                this.addState(serializable.decode(nbt, isClient));
        }

        TState.Type<?> type = TStateRegistry.get(new Identifier(nbt.getString("prevState")));

        if (type == null) {
            TState<?> state = this.createDefaultState();

            this.addState(state);
            this.curState = state.type();
            return;
        }

        this.curState = type;
    }

    public void tick() {
        TEvents.handle(new StargateTickEvent(this));
    }

    public @Nullable TState.Type<?> currentStateType() {
        return curState;
    }

    public @Nullable TState<?> getCurrentState() {
        return stateOrNull(curState);
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

    @Override
    public void dispose() {
        ServerStargateNetwork.get().remove(this.address);
    }
}
