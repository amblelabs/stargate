package dev.amble.stargate.api.v3.testimpl;

import dev.amble.stargate.api.v3.BehaviorRegistry;
import net.minecraft.util.Identifier;

public class Dummy {
    boolean s = false;
    public void m() {
        s = !s;
        var e = new Identifier("test", "shit" + BehaviorRegistry.class.getName().toLowerCase());

    }
}
