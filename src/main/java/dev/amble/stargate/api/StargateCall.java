package dev.amble.stargate.api;

import dev.drtheo.scheduler.api.common.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.core.StargateSounds;
import dev.amble.stargate.core.block.entities.StargateBlockEntity;
import dev.drtheo.scheduler.api.common.TaskStage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a call between two Stargates.
 */
public class StargateCall {
	public final Stargate from;
	public final Stargate to;
	private final long ttl;
	private final TimeUnit ttlUnits;
	private final List<Wiretap> subscribers;

	public StargateCall(Stargate from, Stargate to) {
		this(from, to, 20, TimeUnit.SECONDS);
	}
	/**
	 * Represents a call between two Stargates.
	 * @param from the Stargate making the call
	 * @param to the Stargate being called
	 * @param ttl the time to live of the call
	 * @param ttlUnits the units of the time to live
	 */
	public StargateCall(Stargate from, Stargate to, long ttl, TimeUnit ttlUnits) {
		this.from = from;
		this.to = to;
		this.ttl = ttl;
		this.ttlUnits = ttlUnits;
		this.subscribers = new ArrayList<>();
	}

	public void start() {
		this.setState(Stargate.GateState.PREOPEN);
		Scheduler.get().runTaskLater(this::end, TaskStage.END_SERVER_TICK, ttlUnits, ttl);

		for (Wiretap tap : subscribers) {
			tap.onCallStart(this);
		}

		this.to.playSound(StargateSounds.GATE_OPEN, 0.25f, 1f);
		this.from.playSound(StargateSounds.GATE_OPEN, 0.25f, 1f);

		Scheduler.get().runTaskLater(() -> this.setState(Stargate.GateState.OPEN), TaskStage.END_SERVER_TICK, TimeUnit.TICKS, (long) (20 * 1.725 + 8)); // wait for sfx
	}

	public void end() {
		for (Wiretap tap : subscribers) {
			tap.onCallEnd(this);
		}

		this.to.playSound(StargateSounds.GATE_CLOSE, 0.25f, 1f);
		this.from.playSound(StargateSounds.GATE_CLOSE, 0.25f, 1f);

		Scheduler.get().runTaskLater(() -> this.setState(Stargate.GateState.CLOSED), TaskStage.END_SERVER_TICK, TimeUnit.TICKS, (long) (20 * 1.1));
	}

	protected void setState(Stargate.GateState state) {
		MinecraftServer server = ServerLifecycleHooks.get();
		if (server == null) return;

		setState(server, this.to.getAddress(), state);
		setState(server, this.from.getAddress(), state);
	}
	private static void setState(MinecraftServer server, Address address, Stargate.GateState state) {
		ServerWorld world = server.getWorld(address.pos().getDimension());
		if (world == null) return;
		if (!(world.getBlockEntity(address.pos().getPos()) instanceof StargateBlockEntity entity)) return;

		entity.setGateState(state);
	}

	/**
	 * Subscribe to call events
	 * @return this
	 */
	public StargateCall onEnd(Consumer<StargateCall> consumer) {
		subscribe(new Wiretap() {
			@Override
			public void onCallStart(StargateCall call) {
			}

			@Override
			public void onCallEnd(StargateCall call) {
				consumer.accept(call);
			}
		});
		return this;
	}

	public StargateCall subscribe(Wiretap tap) {
		subscribers.add(tap);
		return this;
	}

	/**
	 * A listener for stargate call events
	 * funny name
	 */
	public interface Wiretap {
		void onCallStart(StargateCall call);
		void onCallEnd(StargateCall call);
	}
}