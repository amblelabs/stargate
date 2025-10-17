package dev.amble.stargate.api.gates;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.GateKernelRegistry;
import dev.amble.stargate.api.StargateLike;
import dev.amble.stargate.api.data.StargateServerData;
import dev.amble.stargate.api.gates.event.init.StargateCreatedEvents;
import dev.amble.stargate.api.gates.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.gates.event.init.StargateUpdateEvents;
import dev.amble.stargate.api.gates.event.tick.StargateTickEvent;
import dev.amble.stargate.api.gates.event.address.StargateRemoveEvent;
import dev.amble.stargate.api.gates.event.state.StateAddedEvent;
import dev.amble.stargate.api.gates.event.state.StateRemovedEvent;
import dev.amble.stargate.api.gates.state.GateState;
import dev.amble.stargate.api.gates.state.stargate.GateIdentityState;
import dev.amble.stargate.api.gates.state.address.GlobalAddressState;
import dev.amble.stargate.api.gates.state.address.LocalAddressState;
import dev.amble.stargate.api.gates.state.stargate.client.ClientGenericGateState;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateYAARs;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import dev.drtheo.yaar.state.TStateContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class Stargate extends TStateContainer.Delegate implements NbtSerializer, StargateLike {

    private final RegistryKey<World> dimension;
    private final BlockPos pos;
    private final Direction facing;

    private TState.Type<? extends GateState<?>> curState;

    private final boolean isClient;
    private boolean dirty;

    public Stargate(ServerWorld world, BlockPos pos, Direction direction) {
        super(StargateYAARs.States.createArrayHolder());

        this.dimension = world.getRegistryKey();
        this.pos = pos;
        this.facing = direction;

        this.isClient = false;

        this.resolve(GateIdentityState.state).shape
                .place(StargateBlocks.RING.getDefaultState(), world, pos, direction);

        GateState<?> state = this.createDefaultState();

        this.addState(state);
        this.curState = state.type();

        this.attachState(true, false);
        StargateCreatedEvents.handleCreation(this);
    }

    public static Stargate fromNbt(NbtCompound nbt, boolean isClient) {
        Identifier id = new Identifier(nbt.getString("Id"));
        return GateKernelRegistry.get().get(id).load(nbt, isClient);
    }

    public Stargate(NbtCompound nbt, boolean isClient) {
        super(StargateYAARs.States.createArrayHolder());

        this.isClient = isClient;

        NbtCompound pos = nbt.getCompound("Pos");
        this.dimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(pos.getString("dimension")));
        this.pos = NbtHelper.toBlockPos(pos);
        this.facing = Direction.byId(pos.getByte("facing"));

        this.updateStates(nbt, isClient);
        this.attachState(false, isClient);

        StargateLoadedEvents.handleLoad(this);
    }

    public <T extends TState<T>> T resolve(TState.Type<T> type) {
        T res = stateOrNull(type);

        if (res == null)
            throw new IllegalStateException("Expected " + type.id());

        return res;
    }

    public void updateStates(NbtCompound nbt, boolean isClient) {
        NbtCompound states = nbt.getCompound("States");

        for (String key : states.getKeys()) {
            if (StargateYAARs.States.get(new Identifier(key)) instanceof TState.NbtBacked<?> serializable) {
                NbtElement state = states.get(key);

                if (state instanceof NbtCompound compound) {
                    this.addState(serializable.decode(compound, isClient));
                } else {
                    this.removeState(serializable);
                }
            }
        }

        Identifier prevStateId = new Identifier(nbt.getString("prevState"));
        TState.Type<?> type = StargateYAARs.States.get(prevStateId);

        if (type == null || this.stateOrNull(type) == null) {
            StargateMod.LOGGER.warn("Bad state: '{}', fixing", prevStateId);
            GateState<?> state = this.createDefaultState();

            this.addState(state);
            this.curState = state.type();

            StargateUpdateEvents.handleUpdate(this);
            return;
        }

        StargateUpdateEvents.handleUpdate(this);
        this.curState = (TState.Type<? extends GateState<?>>) type;
    }

    // TODO: migrate all this to behavior event stuff
    protected void attachState(boolean created, boolean isClient) {
        if (created) {
            this.attachAddressState();
            this.attachIdentity();
        }
        if (isClient) this.attachClientState();
    }

    protected void attachIdentity() {
        this.addState(new GateIdentityState());
    }

    protected void attachAddressState() {
        StargateServerData data = StargateServerData.getOrCreate((ServerWorld) this.world());

        this.addState(data.generateAddress(this.dimension, this.pos, GlobalAddressState::new));
        this.addState(data.generateAddress(this.dimension, this.pos, LocalAddressState::new));
    }

    @Environment(EnvType.CLIENT)
    protected void attachClientState() {
        this.addState(new ClientGenericGateState(false));
    }

    public void tick() {
        TEvents.handle(new StargateTickEvent(this));
    }

    public @Nullable TState.Type<? extends GateState<?>> currentStateType() {
        return curState;
    }

    public @Nullable GateState<?> getCurrentState() {
        return (GateState<?>) stateOrNull((TState.Type<?>) curState);
    }

    public void setCurState(TState.Type<? extends GateState<?>> state) {
        this.curState = state;
    }

    @Override
    @Contract(mutates = "this")
    @SuppressWarnings("UnstableApiUsage")
    public boolean addState(@NotNull TState<?> state) {
        boolean result = super.addState(state);

        if (result)
            TEvents.handle(new StateAddedEvent(this, state));

        return result;
    }

    @Override
    @Contract(mutates = "this")
    @SuppressWarnings("UnstableApiUsage")
    public <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type) {
        T result = super.removeState(type);

        if (result != null)
            TEvents.handle(new StateRemovedEvent(this, result));

        return result;
    }

    @Override
    public @Nullable Stargate asGate() {
        return this;
    }

    public boolean isClient() {
        return isClient;
    }

    // <editor-fold desc="Global position stuff">
    public RegistryKey<World> dimension() {
        return dimension;
    }

    // optimize using WeakReference if this causes a bottleneck
    public @NotNull World world() {
        // trust.
        return Objects.requireNonNull(ServerLifecycleHooks.get().getWorld(this.dimension()));
    }

    public void playSound(SoundEvent event) {
        this.world().playSound(null, this.pos(), event, SoundCategory.BLOCKS);
    }

    public BlockPos pos() {
        return pos;
    }

    public Direction facing() {
        return facing;
    }
    // </editor-fold>

    public boolean dirty() {
        return dirty;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void unmarkDirty() {
        this.dirty = false;
    }

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
        nbt.putString("Id", this.id().toString());

        NbtCompound pos = NbtHelper.fromBlockPos(this.pos);
        pos.putString("dimension", this.dimension.getValue().toString());
        pos.putByte("facing", (byte) this.facing.getId());
        nbt.put("Pos", pos);

        NbtCompound states = new NbtCompound();
        this.forEachState((i, state) -> stateToNbt(states, i, state, isClient));

        nbt.put("States", states);
        nbt.putString("prevState", this.curState.id().toString());
    }

    @SuppressWarnings("rawtypes")
    private <T extends TState<T>> void stateToNbt(NbtCompound nbt, int i, @Nullable TState<T> state, boolean isClient) {
        if (state == null) {
            if (!isClient) // do the diffing only if we're serializing for client
                nbt.put(StargateYAARs.States.get(i).id().toString(), NbtByte.ZERO);

            return;
        }

        TState.Type<T> type = state.type();

        if (!(type instanceof TState.NbtBacked backed))
            return;

        nbt.put(type.id().toString(), backed.encode(state, isClient));
    }

    protected GateState<?> createDefaultState() {
        return new GateState.Closed();
    }

    public abstract Identifier id();

    public void remove(ServerWorld world) {
        this.resolve(GateIdentityState.state).shape.destroy(world, pos, facing);
        TEvents.handle(new StargateRemoveEvent(StargateServerData.get(world), this));
    }

    @Override
    public int hashCode() {
        return pos.hashCode();
    }
}
