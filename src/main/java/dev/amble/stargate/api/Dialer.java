package dev.amble.stargate.api;

import dev.amble.stargate.api.network.Stargate;
import dev.amble.stargate.api.network.StargateNetwork;
import dev.amble.stargate.core.block.StargateBlock;
import dev.drtheo.scheduler.api.common.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.core.StargateSounds;
import dev.drtheo.scheduler.api.common.TaskStage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;

/**
 * For tracking a dialing sequence in progress
 */
public class Dialer implements NbtSync {
	private final Stargate parent;
	private String target;
	private char selected;
	private List<Consumer<Dialer>> subscribers;
	private boolean firstMove;
	private boolean isAutoDialing;
	private Rotation lastRotation;
	private int rotationTicks;
	private int maxRotationTicks;

	public Dialer(Stargate parent) {
		this.selected = GLYPHS[0];
		this.target = "";
		this.subscribers = new ArrayList<>();
		this.parent = parent;
	}
	public Dialer(Stargate parent, NbtCompound nbt) {
		this(parent);

		this.loadNbt(nbt);
	}

	@Override
	public void loadNbt(NbtCompound nbt, boolean isSync) {
		this.selected = nbt.getString("Selected").charAt(0);

		if (isSync) {
			nbt = getSyncNbt(nbt);

			this.target = nbt.getString("Target");
			this.isAutoDialing = nbt.getBoolean("AutoDialing");
			this.rotationTicks = nbt.getInt("RotationTicks");
			this.maxRotationTicks = nbt.getInt("MaxRotationTicks");
			if (nbt.contains("LastRotation")) {
				this.lastRotation = Rotation.valueOf(nbt.getString("LastRotation"));
			} else {
				this.lastRotation = Rotation.FORWARD;
			}
		}
	}

	@Override
	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString("Selected", String.valueOf(this.selected));
		return nbt;
	}

	@Override
	public NbtCompound toSyncNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString("Target", this.target);
		nbt.putBoolean("AutoDialing", this.isAutoDialing);
		nbt.putInt("RotationTicks", this.getRotationTicks());
		nbt.putInt("MaxRotationTicks", this.getMaxRotationTicks());
		if (this.lastRotation == null) this.lastRotation = Rotation.FORWARD;
		nbt.putString("LastRotation", this.lastRotation.name());
		return nbt;
	}

	public void setRotationTicks(int rotationTicks) {
		this.rotationTicks = rotationTicks;
		this.parent.sync();
	}

	public void setMaxRotationTicks(int maxRotationTicks) {
		this.maxRotationTicks = maxRotationTicks;
		this.parent.sync();
	}

	private int getRotationTicks() {
		return rotationTicks;
	}

	private int getMaxRotationTicks() {
		return maxRotationTicks;
	}
	public float getRotationProgress() {
		return MathHelper.clamp((float) this.getRotationTicks() / ((float) this.getMaxRotationTicks()), 0, 1);
	}

	public boolean dial(String address, TimeUnit unit, long delay, boolean lock) {
		if (this.isAutoDialing) return false;
		delay = delay / 2;

		if (address.length() > 1){
			this.clear();
		}

		this.setMaxRotationTicks((int) TimeUnit.TICKS.from(unit, delay));
		this.internalDial(address, 0, lock);
		this.isAutoDialing = true;
		return true;
	}

	public boolean dial(Address address, TimeUnit unit, long delay, boolean lock) {
		return this.dial(address.text(), unit, delay, lock);
	}

	public boolean dial(char target, TimeUnit unit, long delay, boolean lock) {
		return this.dial(String.valueOf(target), unit, delay, lock);
	}

	private void internalDial(String address, int i, boolean lock) {
		if (i == address.length()) {
			this.isAutoDialing = false;
			return;
		}

		if (this.selected == address.charAt(i)) {
			this.rotationTicks = this.getMaxRotationTicks();
		}

		if (i == 0 && this.rotateTowards(address.charAt(i), true)) {
			this.lastRotation = this.lastRotation == Rotation.FORWARD ? Rotation.BACKWARD : Rotation.FORWARD;
		}
		if (this.getRotationTicks() < this.getMaxRotationTicks()) {
			this.setRotationTicks(this.getRotationTicks() + 1);
		} else {
			this.setRotationTicks(0);
			if (this.selected != address.charAt(i)) {
				this.rotateTowards(address.charAt(i), false);
			} else {
				if (lock) {
					this.lock();
					// Schedule the next glyph after the lock sound delay (16 ticks)
					int finalI = i + 1;
					Scheduler.get().runTaskLater(() -> this.internalDial(address, finalI, lock), TaskStage.END_SERVER_TICK,
							TimeUnit.TICKS, 16);
					return;
				}
				i++;
				this.setRotationTicks(-this.getMaxRotationTicks());

				if (i != address.length() && this.rotateTowards(address.charAt(i), true)) {
					this.lastRotation = this.lastRotation == Rotation.FORWARD ? Rotation.BACKWARD : Rotation.FORWARD;
				}
			}
		}

		int finalI = i;
		Scheduler.get().runTaskLater(() -> this.internalDial(address, finalI, lock), TaskStage.END_SERVER_TICK,
				TimeUnit.TICKS,
				this.isCurrentGlyphBeingLocked() ? 2 : 1);
	}

	/**
	 * @return whether the dialer is filled
	 */
	public boolean isComplete() {
		return this.target.length() == 7;
	}

	public Optional<? extends Stargate> toStargate(World world) {
		if (!this.isComplete()) return Optional.empty();

		Address found = StargateNetwork.getInstance(world).getAddress(this.target).orElse(null);
		if (found == null) return Optional.empty();

		return StargateNetwork.getInstance(world).getOptional(found);
	}

	public Optional<StargateCall> complete(Stargate source, World world) {
		Stargate target = this.toStargate(world).orElse(null);

		this.clear();

		if (target == null || world.getBlockState(target.getAddress().pos().getPos()).get(StargateBlock.IRIS)) {
			source.playSound(StargateSounds.GATE_FAIL, 0.25f, 1f);
			return Optional.empty();
		}

		Optional<StargateCall> result = source.dialImmediately(target);

		if (result.isEmpty()) {
			source.playSound(StargateSounds.GATE_FAIL, 0.25f, 1f);
		}

		return result;
	}

	protected void append(char c) {
		if (this.target.length() < 7) {
			this.target += c;

			if (this.isComplete()) {
				this.subscribers.forEach(consumer -> consumer.accept(this));
			}

			this.parent.sync();
		}
	}

	public String clear() {
		String old = this.target;
		this.target = "";
		this.firstMove = true;
		this.isAutoDialing = false;
		this.parent.sync();
		return old;
	}

	/**
	 * Locks a chevron in place
	 * Adds it to the target address
	 */
	public void lock() {
		this.append(this.selected);

		this.parent.playSound(StargateSounds.CHEVRON_LOCK, 0.25f, StargateMod.RANDOM.nextFloat(0.9f, 1.1f));

		Scheduler.get().runTaskLater(() -> this.parent.playSound(StargateSounds.CHEVRON_LOCK, 0.25f, StargateMod.RANDOM.nextFloat(0.9f, 1.1f)),
				TaskStage.END_SERVER_TICK, TimeUnit.TICKS, 16);
	}
	public char getSelected() {
		return this.selected;
	}

	public void setSelected(char glyph) {
		this.selected = glyph;
	}

	/**
	 * @return the index of the selected glyph in GLYPHS
	 */
	public int getSelectedIndex() {
		return Glyph.indexOf(this.selected);
	}

	private char next(boolean simulate) {
		int index = this.getSelectedIndex();
		char next = Glyph.ALL[(index + 1) % Glyph.ALL.length];

		if (!simulate) {
			this.selected = next;
			this.onMove(true);
		}

		return next;
	}
	public char next() {
		char next = this.next(true);
		this.dial(next, false);
		return next;
	}
	private char previous(boolean simulate) {
		int index = this.getSelectedIndex();
		char previous = Glyph.ALL[(index + GLYPHS.length - 1) % GLYPHS.length];

		if (!simulate) {
			this.selected = previous;
			this.onMove(false);
		}

		return previous;
	}
	public char previous() {
		char previous = this.previous(true);
		this.dial(previous, false);
		return previous;
	}

	protected void onMove(boolean next) {
		this.parent.sync();
		this.parent.playSound(StargateSounds.RING_LOOP, 1.25f, StargateMod.RANDOM.nextFloat(1.0f, 1.25f));

		if (this.firstMove) {
			this.parent.playSound(StargateSounds.RING_START, 1.5f, 1.0f);
			this.firstMove = false;
		}
	}

	public int getAmountLocked() {
		return this.target.length();
	}

	public Dialer onCompleted(Consumer<Dialer> consumer) {
		this.subscribers.add(consumer);
		return this;
	}

	/**
	 * @param current the current glyph
	 * @param target the target glyph
	 * @return whether it is faster to call next or previous
	 */
	private boolean isNextFaster(char current, char target) {
		int currentIndex = this.indexOf(current);
		int targetIndex = this.indexOf(target);

		int forwardDistance = (targetIndex - currentIndex + Glyph.ALL.length) % Glyph.ALL.length;
		int backwardDistance = (currentIndex - targetIndex + Glyph.ALL.length) % Glyph.ALL.length;

		return forwardDistance <= backwardDistance;
	}

	private boolean simulateRotateTowards(char target) {
		if (this.selected == target) return false;

		Rotation before = this.lastRotation;
		Rotation after;

		if (this.isNextFaster(this.selected, target)) {
			after = Rotation.FORWARD;
		} else {
			after = Rotation.BACKWARD;
		}

		return before != after;
	}
	private boolean rotateTowards(char target, boolean simulate) {
		if (this.selected == target) return false;

		// Prevent oscillation: only allow direction change if not currently dialing
		if (this.isAutoDialing && this.lastRotation != null) {
			boolean nextFaster = this.isNextFaster(this.selected, target);
			Rotation intended = nextFaster ? Rotation.FORWARD : Rotation.BACKWARD;
			if (this.lastRotation != intended) {
				// Do not change direction while auto-dialing
				if (!simulate) {
					// Still move in the current direction
					if (this.lastRotation == Rotation.FORWARD) {
						this.next(false);
					} else {
						this.previous(false);
					}
				}
				return false;
			}
		}

		if (simulate) {
			return this.simulateRotateTowards(target);
		}

		Rotation before = this.lastRotation;

		if (this.isNextFaster(this.selected, target)) {
			this.next(false);
			this.lastRotation = Rotation.FORWARD;
		} else {
			this.previous(false);
			this.lastRotation = Rotation.BACKWARD;
		}

		return before != this.lastRotation;
	}

	/**
	 * Dials the target address over a period of time
	 * @param address the address to dial
	 */
	public boolean dial(Address address) {
		return this.dial(address, TimeUnit.TICKS, 10, true);
	}

	/**
	 * Dials the target glyph over a period of time
	 * @param c the target glyph
	 * @param lock whether to lock the glyph in place
	 */
	public boolean dial(char c, boolean lock) {
		return this.dial(c, TimeUnit.TICKS, 10, lock);
	}

	public boolean contains(char glyph) {
		return this.target.contains(String.valueOf(glyph));
	}

	/**
	 * @return whether its currently moving to a glyph
	 */
	public boolean isDialing() {
		return this.isAutoDialing;
	}

	/**
	 * @return the last direction the ring was rotating in
	 */
	public Rotation getRotation() {
		return this.lastRotation;
	}

	public boolean isCurrentGlyphBeingLocked() {
		return !this.target.isEmpty() && this.target.charAt(this.target.length() - 1) == this.selected;
	}

	public enum Rotation {
		FORWARD, BACKWARD
	}
}
