package dev.amble.stargate.api;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.data.StargateServerData;
import dev.amble.stargate.api.event.init.StargateCreatedEvents;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.event.init.StargateUpdateEvents;
import dev.amble.stargate.api.event.tick.StargateTickEvent;
import dev.amble.stargate.api.event.address.StargateRemoveEvent;
import dev.amble.stargate.api.event.state.StateAddedEvent;
import dev.amble.stargate.api.event.state.StateRemovedEvent;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.address.GlobalAddressState;
import dev.amble.stargate.api.state.stargate.GateIdentityState;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateYAARs;
import dev.amble.stargate.service.WorldProviderService;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import dev.drtheo.yaar.state.TStateContainer;
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

public class Stargate extends TStateContainer.Delegate implements NbtSerializer, StargateLike {

    private final RegistryKey<World> dimension;
    private final BlockPos pos;
    private final Direction facing;

    private TState.Type<? extends GateState<?>> curState;

    private final boolean isClient;
    private boolean dirty;

    public Stargate(GateIdentityState identity, ServerWorld world, BlockPos pos, Direction direction) {
        super(StargateYAARs.States.createArrayHolder());

        this.isClient = false;

        this.dimension = world.getRegistryKey();
        this.pos = pos;
        this.facing = direction;

        identity.shape.place(StargateBlocks.RING.getDefaultState(), world, pos, direction);
        this.addState(identity);

        this.setGateState(this.fallbackGateState());
        StargateCreatedEvents.handleCreation(this);
    }

    public static Stargate fromNbt(NbtCompound nbt, boolean isClient) {
        Identifier id = new Identifier(nbt.getString("Id"));
        return GateKernelRegistry.get().get(id).load(nbt, isClient);
    }

    public Stargate(GateIdentityState identity, NbtCompound nbt, boolean isClient) {
        super(StargateYAARs.States.createArrayHolder());

        this.isClient = isClient;

        NbtCompound pos = nbt.getCompound("Pos");
        this.dimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(pos.getString("dimension")));
        this.pos = NbtHelper.toBlockPos(pos);
        this.facing = Direction.byId(pos.getByte("facing"));

        this.addState(identity);
        this.updateStates(nbt, isClient);

        StargateLoadedEvents.handleLoad(this);
    }

    public void tick() {
        TEvents.handle(new StargateTickEvent(this));
    }

    public void remove() {
        ServerWorld world = (ServerWorld) this.world();

        this.kernel().shape.destroy(world, pos, facing);
        TEvents.handle(new StargateRemoveEvent(StargateServerData.get(world), this));
    }

    @Override
    public @NotNull Stargate asGate() {
        return this;
    }

    public boolean isClient() {
        return isClient;
    }

    public GateIdentityState kernel() {
        return resolveState(GateIdentityState.state);
    }

    public long globalAddress() {
        return resolveState(GlobalAddressState.state).address();
    }

    public long globalId() {
        return resolveState(GlobalAddressState.state).getAsLong();
    }

    //region Gate state
    public @NotNull TState.Type<? extends GateState<?>> getGateStateType() {
        return curState;
    }

    public @NotNull GateState<?> getGateState() {
        return (GateState<?>) stateOrNull((TState.Type<?>) curState);
    }

    public void setGateState(GateState<?> state) {
        this.curState = state.type();
        this.addState(state);
    }
    //endregion

    //region Global position stuff
    public RegistryKey<World> dimension() {
        return dimension;
    }

    // optimize using WeakReference if this causes a bottleneck
    public @Nullable World world() {
        return WorldProviderService.INSTANCE.getWorld(dimension, isClient);
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
    //endregion

    //region State handling & serialization
    public boolean dirty() {
        return dirty;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void unmarkDirty() {
        this.dirty = false;
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
            this.setGateState(this.fallbackGateState());

            StargateUpdateEvents.handleUpdate(this);
            return;
        }

        StargateUpdateEvents.handleUpdate(this);
        //noinspection unchecked - trust me bro
        this.curState = (TState.Type<? extends GateState<?>>) type;
    }

    private GateState<?> fallbackGateState() {
        return new GateState.Closed();
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

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
        nbt.putString("Id", this.kernel().id.toString());

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
            // do the diffing only if we're serializing for client
            if (isClient) nbt.put(StargateYAARs.States.get(i).id().toString(), NbtByte.ZERO);

            return;
        }

        TState.Type<T> type = state.type();

        if (!(type instanceof TState.NbtBacked backed))
            return;

        //noinspection unchecked
        nbt.put(type.id().toString(), backed.encode(state, isClient));
    }
    //endregion

    @Override
    public int hashCode() {
        return pos.hashCode();
    }
}
