package dev.amble.stargate.api.v3;

import dev.amble.stargate.api.v4.Ordered;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;

public class EnumMap<K extends Ordered, V> {

    private final V[] values;

    public EnumMap(K[] values, IntFunction<V[]> supplier) {
        this.values = supplier.apply(values.length);
    }

    public void map(Function<V, V> func) {
        for (int i = 0; i < values.length; i++) {
            values[i] = func.apply(values[i]);
        }
    }

    /**
     * @return All values associated with each variant of an enum, null if no value
     * is present.
     * @implNote Will return ALL values, including nulls.
     */
    public V[] getValues() {
        return this.values;
    }

    public V put(K k, V v) {
        V prev = values[k.ordinal()];
        values[k.ordinal()] = v;

        return prev;
    }

    public V remove(K k) {
        return put(k, null);
    }

    public V get(K k) {
        return this.get(k.ordinal());
    }

    public V get(int index) {
        return values[index];
    }

    public boolean containsKey(K k) {
        if ((this.size() - 1) < k.ordinal()) return false;

        return this.get(k) != null;
    }

    public void clear() {
        Arrays.fill(this.values, null);
    }

    public int size() {
        return this.values.length;
    }
}