package dev.pavatus.stargate.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Stargate implements StargateCall.Wiretap {
	private final Address address;
	private final List<Subscriber> subscribers;
	private StargateCall call;

	protected Stargate(Address address) {
		this.address = address;
		this.subscribers = new ArrayList<>();
	}
	protected Stargate(NbtCompound nbt) {
		this(Address.fromNbt(nbt.getCompound("Address")));
	}

	/**
	 * @return whether this stargate is available for dialing
	 */
	public boolean isAvailable() {
		return this.call == null;
	}

	/**
	 * Attempts to call another stargate
	 * This creates and links a call, but does not start it.
	 * @param target star gate to call
	 * @return the call if successful
	 */
	public Optional<StargateCall> dial(Stargate target) {
		if (this.address.pos().getDimension() == target.address.pos().getDimension() &&
		this.address.pos().getPos().getX() == target.address.pos().getPos().getX() &&
		this.address.pos().getPos().getY() == target.address.pos().getPos().getY() &&
		this.address.pos().getPos().getZ() == target.address.pos().getPos().getZ()) {
			// cannot call self

			return Optional.empty();
		}
		if (!this.isAvailable()) {
			return Optional.empty();
		}
		if (!target.isAvailable()) {
			return Optional.empty();
		}

		this.call = new StargateCall(this, target);
		target.call = this.call;

		this.call.subscribe(this);
		this.call.subscribe(target);

		this.subscribers.forEach(s -> s.onCallCreate(this, this.call));

		return Optional.of(this.call);
	}

	/**
	 * Attempts to call another stargate
	 * This creates and links a call, but does not start it.
	 * @param target address to call
	 * @return the call if successful
	 */
	public Optional<StargateCall> dial(Address target) {
		return StargateNetwork.getInstance().getOptional(target)
				.flatMap(this::dial);
	}

	public Optional<StargateCall> getCurrentCall() {
		return Optional.ofNullable(this.call);
	}

	public Address getAddress() {
		return address;
	}

	@Override
	public void onCallStart(StargateCall call) {
		if (this.call != call) return;

		this.subscribers.forEach(s -> s.onCallStart(this, call));
	}

	@Override
	public void onCallEnd(StargateCall call) {
		if (this.call != call) return;

		this.subscribers.forEach(s -> s.onCallEnd(this, call));

		this.call = null;
	}

	/**
	 * Disposes of the stargate, unlinks it from everything and prepares it for garbage collection
	 */
	public void dispose() {
		if (this.call != null) {
			this.call.end();
		}
		StargateNetwork.getInstance().remove(this.address);
	}

	/**
	 * Teleports an entity to this stargate
	 * @param entity entity to teleport
	 * @return whether the teleport was successful
	 */
	public boolean teleportHere(LivingEntity entity) {
		TeleportUtil.teleport(entity, this.getAddress().pos());
		return true;
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		nbt.put("Address", address.toNbt());

		return nbt;
	}

	public static Stargate fromNbt(NbtCompound nbt) {
		return new Stargate(nbt);
	}

	public PacketByteBuf writeToPacket(PacketByteBuf buf) {
		buf.writeNbt(this.toNbt());
		return buf;
	}
	public Stargate readFromPacket(PacketByteBuf buf) {
		return new Stargate(buf.readNbt());
	}

	public static Stargate create(Address address) {
		Stargate created = new Stargate(address);
		StargateNetwork.getInstance().add(created);
		return created;
	}

	public interface Subscriber {
		void onCallCreate(Stargate gate, StargateCall call);
		void onCallStart(Stargate gate, StargateCall call);
		void onCallEnd(Stargate gate, StargateCall call);
	}
}
