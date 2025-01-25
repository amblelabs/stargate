package dev.pavatus.stargate.api;

import dev.pavatus.lib.data.DirectedGlobalPos;
import dev.pavatus.lib.util.ServerLifecycleHooks;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;

import java.util.Set;

public class TeleportUtil {
	public static void teleport(LivingEntity entity, DirectedGlobalPos pos) {
		teleport(entity, ServerLifecycleHooks.get().getWorld(pos.getDimension()), pos.getPos().toCenterPos(), RotationPropertyHelper.toDegrees(pos.getRotation())-45);
	}
	public static void teleport(LivingEntity entity, ServerWorld world, Vec3d pos, float yaw) {
		world.getServer().execute(() -> {
			if (entity instanceof ServerPlayerEntity player) {
				teleportPlayer(player, world, pos, yaw, player.getPitch());
				return;
			}

			teleportNonPlayer(entity, world, pos, yaw, entity.getPitch());
		});
	}
	private static void teleportPlayer(ServerPlayerEntity player, ServerWorld world, Vec3d pos, float yaw, float pitch) {
		player.teleport(world, pos.x, pos.y, pos.z, yaw, pitch);
		player.addExperience(0);
		player.getStatusEffects().forEach(effect -> player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), effect)));
		player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
	}
	private static void teleportNonPlayer(LivingEntity entity, ServerWorld world, Vec3d pos, float yaw, float pitch) {
		if (entity.getWorld().getRegistryKey() == world.getRegistryKey()) {
			entity.refreshPositionAndAngles(pos.x, pos.y, pos.z, yaw, pitch);
			return;
		}

		entity.teleport(world, pos.x, pos.y, pos.z, Set.of(), yaw, pitch);
	}
}
