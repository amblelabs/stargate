package dev.amble.stargate.api.address;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.security.SecureRandom;

public class AddressProvider {

    static int BITS_PER_COORD = getBitsNeeded(Glyph.ALL.length);
    static int MASK = (1 << BITS_PER_COORD) - 1;

    private static int getBitsNeeded(int max) {
        return MathHelper.ceil(Math.log(max) / Math.log(2));
    }

    private static int readAt(long address, int index) {
        int shift = index * BITS_PER_COORD;
        return (int) ((address >> shift) & MASK);
    }

    private static long packI(int index, int value) {
        return packI(index, (long) value);
    }

    private static long packI(int index, long value) {
        int shift = index * BITS_PER_COORD;
        return (value << shift);
    }

    public static long pack(String address) {
        char[] chars = address.toCharArray();
        int[] nums = new int[chars.length];

        for (int i = 0; i < chars.length; i++) {
            nums[i] = Glyph.ALPHABET.indexOf(chars[i]); // TODO(perf): use a map or a math trick instead.
        }

        return pack(nums, Glyph.ALL.length);
    }

    public static int length(long address) {
        return Math.toIntExact(address / BITS_PER_COORD);
    }

    public static String asString(long packed) {
        int length = length(packed);
        char[] chars = new char[length];

        for (int i = 0; i < length; i++) {
            chars[i] = Glyph.ALL[AddressProvider.readAt(packed, i)];
        }

        return new String(chars);
    }

    public static long pack(int[] numbers, int k) {
        if (numbers.length == 0) throw new IllegalArgumentException();

        // Validate input
        int maxPossible = (1 << BITS_PER_COORD) - 1;

        for (int num : numbers) {
            if (num < 0 || num > k)
                throw new IllegalArgumentException("Number " + num + " is not in range [0, " + k + "]");

            if (num > maxPossible)
                throw new IllegalArgumentException("Number " + num + " requires more than " + BITS_PER_COORD + " bits");
        }

        // Check if we can fit all numbers in a single long
        if ((long) numbers.length * BITS_PER_COORD > 64) {
            throw new IllegalArgumentException("Cannot pack " + numbers.length + " numbers with " +
                    BITS_PER_COORD + " bits each in a single long (64 bits total)");
        }

        long packed = 0L;

        // Pack numbers from right to left (least significant to most significant)
        for (int i = 0; i < numbers.length; i++) {
            packed |= packI(i, numbers[i]);
        }

        return packed;
    }

    // 1 target (z) + 1 origin (y) + 6 id (x)
    // [xxxxxx][y][z]
    //  012345  6  7
    public static class Local extends AddressProvider {

        // TODO: set random seed to world seed maybe?
        private static final SecureRandom RANDOM = new SecureRandom();

        static int ID_MASK = (1 << BITS_PER_COORD * 6) - 1;

        public static long getId(long packed) {
            return packed & ID_MASK;
        }

        public static char getTargetChar(long packed) {
            return Glyph.ALL[AddressProvider.readAt(packed, 6)];
        }

        public static RegistryKey<World> getTarget(long packed) {
            return GlyphOriginRegistry.get().glyph(getTargetChar(packed));
        }

        public static char getOriginChar(long packed) {
            return Glyph.ALL[AddressProvider.readAt(packed, length(packed) - 1)];
        }

        public static long generate(RegistryKey<World> world) {
            long packed = 0;

            for (int i = 0; i < 6; i++) {
                packed |= AddressProvider.packI(i, RANDOM.nextLong(Glyph.ALL.length));
            }

            char poi = GlyphOriginRegistry.get().glyph(world);
            return packed | AddressProvider.packI(6, poi);
        }
    }

    // 1 target (y) + 6 id (x)
    // [xxxxxxxx][y]
    //  01234567  8
    public static class Global extends AddressProvider {

        // TODO: set random seed to world seed maybe?
        private static final SecureRandom RANDOM = new SecureRandom();

        static int ID_MASK = (1 << BITS_PER_COORD * 8) - 1;

        public static long getId(long packed) {
            return packed & ID_MASK;
        }

        public static char getTargetChar(long packed) {
            return Glyph.ALL[AddressProvider.readAt(packed, 8)];
        }

        public static RegistryKey<World> getTarget(long packed) {
            return GlyphOriginRegistry.get().glyph(getTargetChar(packed));
        }

        public static long generate(RegistryKey<World> dim) {
            long packed = 0;

            for (int i = 0; i < 8; i++) {
                packed |= AddressProvider.packI(i, RANDOM.nextLong(Glyph.ALL.length));
            }

            char poi = GlyphOriginRegistry.get().glyph(dim);
            return packed | AddressProvider.packI(8, poi);
        }
    }
}