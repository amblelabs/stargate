package dev.amble.stargate.block.entities;

import dev.amble.stargate.block.DialingComputerBlock;
import dev.amble.stargate.init.StargateBlockEntities;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DialingComputerBlockEntity extends NearestLinkingBlockEntity {

    public DialingComputerBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.COMPUTER, pos, state, true);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, DialingComputerBlock.OPEN, PacketByteBufs.create());
            return ActionResult.CONSUME;
        }

        return ActionResult.SUCCESS;
    }
}
