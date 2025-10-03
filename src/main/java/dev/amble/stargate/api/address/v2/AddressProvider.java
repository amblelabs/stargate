package dev.amble.stargate.api.address.v2;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Map;

public class AddressProvider {

    static String ALPHABET_ALL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ[]{}:;$()%";
    static char[] ALPHABET = ALPHABET_ALL.toCharArray();

    static int BITS_PER_COORD = getBitsNeeded(ALPHABET.length);

    private static int getBitsNeeded(int max) {
        return MathHelper.ceil(Math.log(max) / Math.log(2));
    }

    // long (address) = 6 int (∈ La) + self poi (∈ La)
    public long getAddress(Identifier world, BlockPos pos) {
        int[] buf = new int[7];

        buf[6] = ALPHABET_ALL.indexOf(getPOI(world));
        int[] posCombed = VectorToUniqueBase36.vectorToUniqueBase36(pos);

        System.out.println(Arrays.toString(posCombed));
        System.arraycopy(posCombed, 0, buf, 0, posCombed.length);

        return pack(buf, ALPHABET.length);
    }

    public static void readAddress(long address, char[] chars, int len) {
        int mask = (1 << BITS_PER_COORD) - 1;

        for (int i = 0; i < len; i++) {
            int shift = i * BITS_PER_COORD;
            int num = (int) ((address >> shift) & mask);

            System.out.println(num);
            chars[i] = ALPHABET[num];
        }
    }

    /*public static char getPOI(RegistryKey<World> from) {
        return GlyphOriginRegistry.getInstance().get(from.getValue()).glyph();
    }

    public static char getGalaxy(RegistryKey<World> from) {
        return GlyphOriginRegistry.getInstance().get(from.getValue()).glyph();
    }*/

    static final Identifier OVERWORLD = new Identifier("minecraft", "overworld");
    static final Identifier NETHER = new Identifier("minecraft", "nether");

    static final Map<Identifier, Character> MAP = Map.of(
            OVERWORLD, 'Q',
            NETHER, 'W'
    );

    public static char getPOI(Identifier from) {
        return MAP.get(from);
    }

    public long pack(int[] numbers, int k) {
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
            int shift = i * BITS_PER_COORD;
            packed |= ((long) numbers[i] << shift);
        }

        return packed;
    }

    public static class C6 extends AddressProvider {

        public static String getAddress(long packed) {
            char[] c = new char[6];
            readAddress(packed, c, 6);
            return new String(c);
        }
    }

    /**
     * 7 character address = 6 points (coordinates coded) + 1 POI
     */
    public static class C7 extends AddressProvider {

        private static void readAddress(long packed, char[] c, int len, Identifier origin) {
            readAddress(packed, c, len);
            c[6] = getPOI(origin);
        }

        public static String getAddress(long packed, Identifier origin) {
            char[] c = new char[7];

            // every character has to be unique, so only (36 - 1) characters are available
            readAddress(packed, c, 6, origin);
            return new String(c);
        }
    }

    public static class C8 extends AddressProvider {

        public static String getAddress(long packed, Identifier origin) {
            char[] c = new char[8];

            // every character has to be unique, so only (36 - 1) characters are available
            readAddress(packed, c, 7);

            c[7] = getPOI(origin);

            return new String(c);
        }
    }

    public static void main(String[] args) {
        int[] pos = new int[] { 123, 123, 123 };

        long packed = new AddressProvider().getAddress(OVERWORLD, new BlockPos(pos[0], pos[1], pos[2]));

        System.out.println(Arrays.toString(pos));
        System.out.println(packed);
        System.out.println("6c: " + C6.getAddress(packed));
        System.out.println("7c: " + C7.getAddress(packed, NETHER));
        System.out.println("8c: " + C8.getAddress(packed, NETHER));


    }
}
