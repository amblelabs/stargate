package dev.amble.stargate.api.address.v2;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.math.BlockPos;

public class AddressPositionHelper {

    static final int MAX_COORD = 15_000_000;

    private static int verifyPos(int pos) {
        if (pos < -MAX_COORD || pos > MAX_COORD) {
            throw new IllegalArgumentException("Pos must be in range [-" + MAX_COORD + ", " + MAX_COORD + "], got " + pos);
        }
        return pos;
    }

    public static void pos2Addr(BlockPos pos, int[] buf, int len, int vars, int partition, long... ignore) {
        positionToAddress(getBlockId(pos, partition), buf, len, vars, ignore);
    }

    public static void positionToAddress(long blockId, int[] buf, int len, int vars, long... ignore) {
        // Generate 6 unique values from 0-35 based on blockId
        LongSet used = new LongOpenHashSet(ignore);

        // Use blockId as the seed for generating the sequence
        long current = blockId;
        
        for (int i = 0; i < len; i++) {
            // Generate candidate value and ensure it's not already used
            long candidate = (current + i * 1234567L) % vars;
            
            // If already used, find next available
            while (used.contains(candidate)) {
                candidate = (candidate + 1) % vars;
            }

            buf[i] = (int) candidate;
            used.add(candidate);
            
            // Update current for next iteration
            current = (current * 16807) % 2147483647L;  // Standard LCG multiplier
        }
    }

    private static long getBlockId(BlockPos pos, int partitionSize) {
        return getBlockId(verifyPos(pos.getX()), verifyPos(pos.getY()), verifyPos(pos.getZ()), partitionSize);
    }

    private static long getBlockId(int x, int y, int z, int partitionSize) {
        if (partitionSize <= 0) throw new IllegalArgumentException("partitionSize must be positive");

        int blockX = x / partitionSize;
        int blockY = y / partitionSize;
        int blockZ = z / partitionSize;

        int minBlock = -MAX_COORD / partitionSize;
        int maxBlock = MAX_COORD / partitionSize;

        long rangeBlocks = maxBlock - minBlock + 1;

        long blockXShifted = blockX - minBlock;  // 0..rangeBlocks-1
        long blockYShifted = blockY - minBlock;  // 0..rangeBlocks-1
        long blockZShifted = blockZ - minBlock;  // 0..rangeBlocks-1

        return blockXShifted * rangeBlocks * rangeBlocks +
                blockYShifted * rangeBlocks +
                blockZShifted;
    }
}