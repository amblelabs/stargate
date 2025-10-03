package dev.amble.stargate.api.address.v2;

import dev.amble.stargate.api.address.Glyph;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class VectorToUniqueBase36 {

    public static long maxCombinations(int length) {
        long res = 1;
        for (int i = 0; i < length; i++) {
            res *= Glyph.ALL.length - i;
        }

        return res;
    }

    // one character is taken by the POI
    public static final long MAX_COMBS = maxCombinations(6);
    public static final int MAX_CHARS = Glyph.ALL.length;
    public static final int MAX_LEN = 6;

    public static long maxK(int maxY) {
        int foundXZ = -1;
        for (int xz = 0; xz < 30_000_000; xz++) {
            if ((long) xz * xz * maxY > MAX_COMBS) break;

            foundXZ = xz;
        }

        return foundXZ;
    }

    public static int[] vectorToUniqueBase36(BlockPos pos, int xMax, int yMax, int zMax) {
        long index = (long) pos.getX() * yMax * zMax + (long) pos.getY() * zMax + pos.getZ();

        // Verify the index is within bounds
        long totalCombinations = (long) xMax * yMax * zMax;
        if (index >= MAX_COMBS || totalCombinations > MAX_COMBS) {
            throw new IllegalArgumentException(
                    "Input range too large. Max combinations: " + MAX_COMBS + ", yours: " + totalCombinations);
        }

        IntList available = new IntArrayList();
        for (int i = 0; i < MAX_LEN * MAX_LEN; i++) {
            available.add(i);
        }

        int[] result = new int[6];
        long n = index;

        for (int i = 0; i < 6; i++) {
            int remainingToPick = 5 - i;
            long perms = 1;
            for (int j = 0; j < remainingToPick; j++) {
                perms *= (MAX_CHARS - 1 - i - j);
            }

            int digitIndex = (int)(n / perms);
            result[i] = available.getInt(digitIndex); // Use the available digit
            available.removeInt(digitIndex); // Remove used digit from available
            n = n % perms;
        }

        return result;
    }

    public static BlockPos uniqueBase36ToVector(int[] digits, int xMax, int yMax, int zMax) {
        // Convert permutation back to index
        IntList available = new IntArrayList(36);
        for (int i = 0; i < MAX_CHARS; i++) {
            available.add(i);
        }

        long index = 0;

        for (int i = 0; i < digits.length; i++) {
            int digit = digits[i];

            // Find position of this digit in available digits
            int pos = available.indexOf(digit);

            if (pos == -1) {
                throw new IllegalArgumentException("Invalid permutation: digit " + digit + " not available at position " + i);
            }

            int remainingToPick = MAX_LEN - 1 - i;
            long perms = 1;

            for (int j = 0; j < remainingToPick; j++) {
                perms *= (MAX_CHARS - 1 - i - j);
            }

            index += (long) pos * perms;
            available.removeInt(pos); // Remove used digit
        }

        // Convert single index back to 3D coordinates with different ranges
        long yzRange = (long) yMax * zMax;

        int x = (int)(index / yzRange);
        long remaining = index % yzRange;
        int y = (int)(remaining / zMax);
        int z = (int)(remaining % zMax);

        return new BlockPos(x, y, z);
    }

    public static int[] vectorToUniqueBase36(BlockPos pos, int maxXZ, int maxY) {
        return vectorToUniqueBase36(pos, maxXZ, maxY, maxXZ);
    }

    public static BlockPos uniqueBase36ToVector(int[] s, int maxXZ, int maxY) {
        return uniqueBase36ToVector(s, maxXZ, maxY, maxXZ);
    }

    public static int[] vectorToUniqueBase36(BlockPos pos) {
        return vectorToUniqueBase36(pos, 21621, 3);
    }

    public static BlockPos uniqueBase36ToVector(int[] s) {
        return uniqueBase36ToVector(s, 21621, 3);
    }

    public static void main(String[] args) {
        try {
            System.out.println(maxK(3));
            int[] result = vectorToUniqueBase36(new BlockPos(500, 0, 700), 21621, 3);
            System.out.println("Encoded: " + Arrays.toString(result));
            
            BlockPos original = uniqueBase36ToVector(result, 21621, 3);
            System.out.println("Decoded: [" + original + "]");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}