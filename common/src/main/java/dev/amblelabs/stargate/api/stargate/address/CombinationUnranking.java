package dev.amblelabs.stargate.api.stargate.address;

import java.math.BigInteger;

public class CombinationUnranking {

    private static long factorial(long number) {
        if (number <= FACT.length)
            return FACT[(int) number - 1];

        long result = FACT[FACT.length - 1];
        for (long i = result + 1; i <= number; i++) {
            result *= i;
        }

        return result;
    }

    // good old C^rn = n!/(r!*(n-r)!)
    public static long getCombinations(int total, int density) {
        if (total < density) throw new IllegalArgumentException();
        if (total == density) return total == 0 ? 0 : 1;

        // e.g. total = 25, density = 2; 25 > 2*2 = 25 > 4; diff = 23
        if (total > 2 * density) {
            long factorial = 1;
            for (int i = (total - density + 1); i < total; i++) {
                factorial *= i;
            }

            return factorial / factorial(density);
        } else if (total == 2 * density) { // e.g. total = 24, density = 12; diff = 12
            long factorial = 1;
            for (int i = (total - density + 1); i < total; i++) {
                factorial *= i;
            }

            return factorial / factorial(density);
        } else { // e.g. total = 24, density = 20; diff = 4
            long factorial = 1;
            for (int i = density + 1; i < total; i++) {
                factorial *= i;
            }

            return factorial / factorial(total - density);
        }
    }

    // ---------- Exact binomial for small k (k < 10) ----------
    private static BigInteger binom(long n, int k) {
        if (k < 0 || k > n) return BigInteger.ZERO;
        if (k > n - k) k = (int)(n - k);
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i <= k; i++) {
            res = res.multiply(BigInteger.valueOf(n - k + i))
                     .divide(BigInteger.valueOf(i));
        }
        return res;
    }

    // ---------- Factorials for small k ----------
    private static final long[] FACT = new long[11];

    static {
        FACT[0] = 1;
        for (int i = 1; i < FACT.length; i++) {
            FACT[i] = FACT[i-1] * i;
        }
    }

    // ---------- Newton‑Raphson inversion of C(y, r) ----------
    private static int largestYWithBinomLessThan(BigInteger target, int r, int maxY) {
        // Return the largest y in [0, maxY] such that C(y, r) < target.
        if (target.compareTo(BigInteger.ONE) <= 0)
            return Math.min(r - 1, maxY);

        if (r == 1) {
            long val = target.longValue() - 1;
            return (int) Math.min(val, maxY);
        }

        // Initial guess using real‑valued approximation: y ≈ (r! * target)^(1/r)
        double logFact = Math.log(FACT[r]);
        double logTarget = Math.log(target.doubleValue());
        double yDbl;
        if (Double.isInfinite(logTarget)) {
            yDbl = maxY * 0.9;
        } else {
            yDbl = Math.exp((logFact + logTarget) / r);
        }
        int y = (int) Math.max(0, Math.min(maxY, yDbl));
        if (y < r) y = r;

        // Newton iteration (20 steps enough for r ≤ 9)
        for (int iter = 0; iter < 20; iter++) {
            BigInteger c = binom(y, r);
            if (c.equals(BigInteger.ZERO)) {
                y = (y + maxY) / 2;
                if (y < r) y = r;
                continue;
            }
            // discrete derivative: f'(y) ≈ C(y,r) * r / (y + 1 - r)
            BigInteger diff = c.subtract(target);
            long denom = (y + 1L - r) * r;
            if (denom == 0) break;
            BigInteger delta = diff.multiply(BigInteger.valueOf(y + 1L - r))
                                   .divide(BigInteger.valueOf(denom));
            long deltaLong = delta.longValue();
            if (deltaLong == 0) {
                if (c.compareTo(target) < 0) y++;
                else y--;
            } else {
                y = (int) (y - deltaLong);
            }
            y = Math.clamp(y, 0, maxY);
            if (y < r && target.compareTo(BigInteger.ONE) > 0) y = r;
        }

        // Final exact adjustment: find largest y with C(y,r) < target
        while (y <= maxY && binom(y, r).compareTo(target) < 0) y++;
        y--;
        if (y < 0) y = 0;
        return y;
    }

    // ---------- Main unranking method ----------
    public static int[] unrankCombination(int n, int k, BigInteger idx) {
        if (k < 0 || k > n)
            throw new IllegalArgumentException("Invalid k");
        if (idx.compareTo(BigInteger.ZERO) < 0)
            throw new IllegalArgumentException("Index must be non‑negative");

        BigInteger total = binom(n, k);
        if (idx.compareTo(total) >= 0)
            throw new IllegalArgumentException("Index out of range");

        if (k == 0) return new int[0];
        if (k == 1) {
            return new int[]{ idx.intValue() };
        }

        // ---------- Special case k = 2 : O(1) with BigInteger sqrt ----------
        if (k == 2) {
            // Solve a*(2n - a - 1)/2 <= idx < (a+1)*(2n - (a+1) - 1)/2
            BigInteger twoN = BigInteger.valueOf(2L * n - 1);
            BigInteger disc = twoN.pow(2).subtract(BigInteger.valueOf(8).multiply(idx));
            BigInteger sqrtDisc = disc.sqrt(); // Java 9+
            BigInteger a = twoN.subtract(sqrtDisc).divide(BigInteger.TWO);

            // Guard against rounding errors
            while (a.compareTo(BigInteger.valueOf(n - 1)) < 0) {
                BigInteger nextA = a.add(BigInteger.ONE);
                long nA = nextA.longValue();
                if (nextA.multiply(BigInteger.valueOf(2L * n - nA - 1))
                         .divide(BigInteger.TWO)
                         .compareTo(idx) > 0) break;
                a = nextA;
            }
            while (a.compareTo(BigInteger.ZERO) > 0) {
                BigInteger prevA = a.subtract(BigInteger.ONE);
                long nA = prevA.longValue();
                if (prevA.multiply(BigInteger.valueOf(2L * n - nA - 1))
                         .divide(BigInteger.TWO)
                         .compareTo(idx) <= 0) break;
                a = prevA;
            }

            int aInt = a.intValue();
            long S_a = (long) aInt * (2L * n - aInt - 1) / 2;
            int bInt = aInt + 1 + (int)(idx.longValue() - S_a);
            return new int[]{ aInt, bInt };
        }

        // ---------- General k > 2 : Newton unranking ----------
        int[] result = new int[k];
        int start = 0;
        int rem = k;
        BigInteger currentIdx = idx;

        for (int pos = 0; pos < k; pos++) {
            BigInteger totalRem = binom(n - start, rem);          // C(n-start, rem)
            BigInteger target = totalRem.subtract(currentIdx);    // totalRem - idx

            // Find largest y with C(y, rem) < target, y in [0, n-1]
            int y = largestYWithBinomLessThan(target, rem, n - 1);
            int x = n - y - 1;
            if (x < start) x = start;

            result[pos] = x;

            // Subtract combinations with first element < x
            BigInteger before = totalRem.subtract(binom(n - x, rem));
            currentIdx = currentIdx.subtract(before);

            start = x + 1;
            rem--;
        }
        return result;
    }

    // ---------- Example usage ----------
    public static void main(String[] args) {
        String[] items = {"1", "2", "3", "4", "5", "6"};
        int n = items.length;
        int k = 3;

        int[] indices = unrankCombination(n, k, BigInteger.TEN);
        StringBuilder sb = new StringBuilder();
        for (int i : indices) {
            sb.append(items[i]);
        }
        System.out.println(sb); // Prints "123456101112"
    }
}