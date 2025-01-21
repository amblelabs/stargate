package dev.pavatus.stargate.api;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.pavatus.stargate.core.block.StargateBlock;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

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
		this(from, to, 5, TimeUnit.SECONDS);
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
		Scheduler.get().runTaskLater(this::end, ttlUnits, ttl);

		for (Wiretap tap : subscribers) {
			tap.onCallStart(this);
		}
	}

	public void end() {
		for (Wiretap tap : subscribers) {
			tap.onCallEnd(this);
		}
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