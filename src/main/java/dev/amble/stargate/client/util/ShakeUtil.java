package dev.amble.stargate.client.util;

import dev.amble.stargate.api.kernels.GateState;
import dev.amble.stargate.api.network.ClientStargate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ShakeUtil {
	private static final float SHAKE_CLAMP = 45.0f; // Adjust this value to set the maximum shake angle
	private static final float SHAKE_INTENSITY = 0.45f; // Adjust this value to control the intensity of the shake
	private static final int MAX_DISTANCE = 16; // The radius from the stargate where the player will feel the shake
	private static final int MAX_DISTANCE_SQUARED = MathHelper.square(MAX_DISTANCE);

	public static void shakeFromGate(@NotNull ClientStargate gate) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		float multiplier;

		if (gate.state() instanceof GateState.Closed closed)
			multiplier = closed.locked() / 7f;
		else if (gate.state() instanceof GateState.PreOpen)
			multiplier = 1;
		else
			return;

		double distance = gate.address().pos().getPos().getSquaredDistance(player.getBlockPos());

		if (distance > MAX_DISTANCE_SQUARED)
			return;

		shake((1f - (float) Math.sqrt(distance / MAX_DISTANCE_SQUARED)) * multiplier);
	}

	public static void shake(float scale) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null)
			return;

		float targetPitch = getShakeX(client.player.getPitch(), scale);
		float targetYaw = getShakeY(client.player.getYaw(), scale);

		client.player.setPitch(MathHelper.lerp(SHAKE_INTENSITY, client.player.getPitch(), targetPitch));
		client.player.setYaw(MathHelper.lerp(SHAKE_INTENSITY, client.player.getYaw(), targetYaw));
	}

	private static float getShakeY(float baseYaw, float scale) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null)
			return baseYaw;

		float temp = (client.player.getRandom().nextFloat() * scale);
		float shakeYaw = baseYaw + (client.player.getRandom().nextBoolean() ? temp : -temp);

		return MathHelper.clamp(shakeYaw, baseYaw - SHAKE_CLAMP, baseYaw + SHAKE_CLAMP);
	}

	private static float getShakeX(float basePitch, float scale) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null)
			return basePitch;

		float temp = (client.player.getRandom().nextFloat() * (scale / 2));
		float shakePitch = basePitch + (client.player.getRandom().nextBoolean() ? temp : -temp);

		return MathHelper.clamp(shakePitch, basePitch - SHAKE_CLAMP, basePitch + SHAKE_CLAMP);
	}
}
