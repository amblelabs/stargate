package dev.amblelabs.stargate.common.lib;

import com.google.gson.JsonParser;
import dev.amblelabs.stargate.api.ecs.NbtPrototype;
import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

// stargayting it rn
/**
 * A temp class TODO: remove this class
 * until datapack loading is done
 */
public class StargatePrototypes {

    public static void registerPrototypes(Registry<PrototypeRegistryEntry> s, BiConsumer<PrototypeRegistryEntry, ResourceLocation> r) {
        for (var e : PROTOTYPES.entrySet()) {
            ResourceLocation key = e.getKey();
            r.accept(e.getValue().build(s), e.getKey());
        }
    }

    private static final Map<ResourceLocation, NbtPrototype> PROTOTYPES = new LinkedHashMap<>();

    public static final NbtPrototype MILKY_WAY = prototype("milky_way", """
            {
                "states": {
                    "stargate:gecko": {
                        "model": "stargate:block/stargate",
                        "animation": "stargate:block/stargate",
                        "texture": "stargate:block/milky_way"
                    }
                }
            }""");

    public static final NbtPrototype DESTINY = prototype("destiny", """
            {
                "states": {
                    "stargate:gecko": {
                        "model": "stargate:block/stargate",
                        "animation": "stargate:block/stargate",
                        "texture": "stargate:block/destiny"
                    }
                }
            }""");

    public static final NbtPrototype PEGASUS = prototype("pegasus", """
            {
                "states": {
                    "stargate:gecko": {
                        "model": "stargate:block/stargate",
                        "animation": "stargate:block/stargate",
                        "texture": "stargate:block/pegasus"
                    }
                }
            }""");

    private static NbtPrototype prototype(String name, String content) {
        ResourceLocation loc = modLoc(name);
        NbtPrototype proto = NbtPrototype.tryDeserialize(loc, JsonParser.parseString(content).getAsJsonObject());
        NbtPrototype old = PROTOTYPES.put(loc, proto);

        if (old != null) throw new IllegalArgumentException("Duplicate id " + name);

        return proto;
    }
}
