package dev.drtheo.yaar.benchmarks;

import java.util.Arrays;
import java.util.function.Consumer;

public class SparseSet<T> {
    private final Object[] dense;  // actual data
    private final int[] sparse;    // maps entity ID to dense index
    private final int[] reverse;   // maps dense index back to entity ID
    private int count = 0;
    
    public SparseSet(int maxEntities) {
        dense = new Object[maxEntities];
        sparse = new int[maxEntities];
        reverse = new int[maxEntities];

        Arrays.fill(sparse, -1);
    }
    
    public void add(int entityId, T component) {
        if (has(entityId)) {
            dense[sparse[entityId]] = component;
        } else {
            int denseIndex = count++;
            dense[denseIndex] = component;
            sparse[entityId] = denseIndex;
            reverse[denseIndex] = entityId;
        }
    }
    
    @SuppressWarnings("unchecked")
    public T get(int entityId) {
        int denseIndex = sparse[entityId];
        return denseIndex != -1 ? (T) dense[denseIndex] : null;
    }
    
    public boolean has(int entityId) {
        return sparse[entityId] != -1;
    }

    public void remove(int entityId) {
        int denseIndex = sparse[entityId];

        if (denseIndex == -1)
            return;

        // Move last element to removed position to maintain compactness
        int lastIndex = --count;

        if (denseIndex != lastIndex) {
            // Move last element to the removed position
            Object lastComponent = dense[lastIndex];
            int lastEntityId = reverse[lastIndex];

            dense[denseIndex] = lastComponent;
            sparse[lastEntityId] = denseIndex;
            reverse[denseIndex] = lastEntityId;
        }

        // Clear the moved-from position
        dense[lastIndex] = null;
        sparse[entityId] = -1;
    }

    public void forEach(Consumer<T> consumer) {
        for (int i = 0; i < count; i++) {
            consumer.accept((T) dense[i]);
        }
    }

    public static void main(String[] args) {
        var ss = new SparseSet<>(10);
        ss.add(0, "0");
        ss.add(2, "2");
        ss.add(4, "4");

        ss.forEach(System.out::println);
    }
}