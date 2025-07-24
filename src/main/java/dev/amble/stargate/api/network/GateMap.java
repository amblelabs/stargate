package dev.amble.stargate.api.network;

import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.v2.Stargate;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GateMap<T extends Stargate> {

    protected final Map<UUID, T> idMap = new HashMap<>();
    protected final Map<String, T> addrMap = new HashMap<>();

    public @Nullable T get(Address address) {
        return get(address.id());
    }

    public @Nullable T get(UUID id) {
        return idMap.get(id);
    }

    public @Nullable T get(String addr) {
        return addrMap.get(addr);
    }

    public int size() {
        return idMap.size();
    }

    public Iterable<T> values() {
        return idMap.values();
    }

    public void clear() {
        this.idMap.clear();
        this.addrMap.clear();
    }

    public static class Mutable<T extends Stargate> extends GateMap<T> {

        public @Nullable T put(T gate) {
            Address address = gate.address();
            T prev = this.idMap.put(address.id(), gate);
            this.addrMap.put(address.text(), gate);
            //this.gates.add(gate);

            return prev;
        }

        public @Nullable T remove(T gate) {
            return this.remove(gate.address());
        }

        public @Nullable T remove(Address address) {
            this.addrMap.remove(address.text());
            return this.idMap.remove(address.id());
        }

        public @Nullable T remove(UUID id, String addr) {
            return this.remove(new Address(id, addr, null));
        }
    }
}
