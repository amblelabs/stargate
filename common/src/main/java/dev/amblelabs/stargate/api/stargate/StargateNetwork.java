package dev.amblelabs.stargate.api.stargate;

import dev.amblelabs.stargate.api.ecs.event.StargateTickEvents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StargateNetwork {

    // TODO: address system
    protected final Map<UUID, Stargate> lookup = new HashMap<>();

    public void tick() {
        for (Stargate stargate : lookup.values()) {
            this.tick(stargate);
        }
    }

    protected void tick(Stargate stargate) {
        StargateTickEvents.notify(events -> events.tick(stargate));
    }

    public void remove(UUID id) {
        this.lookup.remove(id);
    }

    public boolean contains(UUID id) {
        return lookup.containsKey(id);
    }

    private void clear() {
        this.lookup.clear();
    }

    public @Nullable Stargate get(UUID key) {
        return lookup.get(key);
    }

    public interface ManagerLevel {
        StargateNetwork stargate$getNetwork();
    }

    public static StargateNetwork get(Level level) {
        return ((ManagerLevel) level).stargate$getNetwork();
    }
}
