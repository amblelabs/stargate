package dev.amble.stargate.api;

import com.google.gson.JsonParser;
import dev.amble.lib.AmbleKit;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.Glyph;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class GlyphOriginRegistry implements IdentifiableResourceReloadListener {

    private final RegistryKey<World>[] REGISTRY = new RegistryKey[Glyph.ALL.length];

    private static GlyphOriginRegistry instance;

    public static GlyphOriginRegistry get() {
        return instance;
    }

    static {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(instance = new GlyphOriginRegistry());
    }

    private GlyphOriginRegistry() { }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        register(World.OVERWORLD, 'Q');

        for (Identifier id : manager.findResources("poi", f -> f.getPath().endsWith(".json")).keySet()) {
            try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                char created = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject()
                        .getAsJsonPrimitive("glyph").getAsCharacter();

                this.register(RegistryKey.of(RegistryKeys.WORLD, id), created);
            } catch (Exception e) {
                AmbleKit.LOGGER.error("Error occurred while loading resource json {}", id.toString(), e);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    public void register(RegistryKey<World> key, char c) {
        REGISTRY[c] = key;
    }

    public RegistryKey<World> glyph(char c) {
        return REGISTRY[c];
    }

    public char glyph(RegistryKey<World> key) {
        for (char i = 0; i < REGISTRY.length; i++) {
            RegistryKey<World> dim = REGISTRY[i];
            if (key.equals(dim)) return i;
        }

        return '*';
    }

    public void allocate(RegistryKey<World> key) {
        Character free = null;

        // O(36). ow.
        for (char i = 0; i < REGISTRY.length; i++) {
            if (REGISTRY[i].equals(key))
                return;

            if (REGISTRY[i] == null) free = i;
        }

        if (free == null)
            throw new IllegalStateException();

        REGISTRY[free] = key;
    }

    @Override
    public Identifier getFabricId() {
        return StargateMod.id("poi");
    }
}
