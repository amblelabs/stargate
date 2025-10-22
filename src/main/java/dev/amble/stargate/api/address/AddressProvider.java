package dev.amble.stargate.api.address;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
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
        return (int) ((address >> shift) & MASK) - 1;
    }

    private static long packI(int index, int value) {
        return packI(index, (long) value);
    }

    private static long packI(int index, long value) {
        int shift = index * BITS_PER_COORD;
        return ((value + 1) << shift);
    }

    public static long pack(String address, int len) {
        char[] chars = address.toCharArray();
        int[] nums = new int[len];

        for (int i = 0; i < len; i++) {
            nums[i] = indexOf(chars[i]); // TODO(perf): use a map or a math trick instead.
        }

        return pack(nums, Glyph.ALL.length);
    }

    public static long pack(int[] numbers) {
        return pack(numbers, Glyph.ALL.length);
    }

    public static int length(long address) {
        return (63 - Long.numberOfLeadingZeros(address)) / BITS_PER_COORD + 1;
    }

    public static String asString(long packed, int len) {
        char[] chars = new char[len];

        for (int i = 0; i < len; i++) {
            chars[i] = Glyph.ALL[AddressProvider.readAt(packed, i)];
        }

        return new String(chars);
    }

    protected static int indexOf(char c) {
        return Glyph.ALPHABET.indexOf(c); // TODO(perf): use a math trick or something
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

        /// FIXME: make this better, maybe?
        public static long generate(RegistryKey<World> world) {
            IntSet set = new IntArraySet();

            char poi = GlyphOriginRegistry.get().glyph(world);
            int poiI = indexOf(poi);

            while (set.size() != 5) {
                int a = RANDOM.nextInt(Glyph.ALL.length);

                if (a == poiI)
                    continue;

                set.add(a);
            }

            long packed = pack(set.toArray(new int[0]));
            return packed | AddressProvider.packI(6, poiI);
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

        /// FIXME: make this better, maybe?
        public static long generate(RegistryKey<World> dim) {
            IntSet set = new IntArraySet();

            char poi = GlyphOriginRegistry.get().glyph(dim);
            int poiI = indexOf(poi);

            while (set.size() != 8) {
                int a = RANDOM.nextInt(Glyph.ALL.length);

                if (a == poiI)
                    continue;

                set.add(a);
            }

            long packed = pack(set.toArray(new int[0]));
            return packed | AddressProvider.packI(8, poiI);
        }

        public static String asString(long packed) {
            return AddressProvider.asString(packed, 9);
        }
    }
}