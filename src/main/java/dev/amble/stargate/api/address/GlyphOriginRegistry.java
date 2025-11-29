package dev.amble.stargate.api.address;

import com.google.gson.JsonParser;
import dev.amble.lib.AmbleKit;
import dev.amble.stargate.StargateMod;
import it.unimi.dsi.fastutil.objects.Object2CharArrayMap;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
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

    @SuppressWarnings("unchecked")
    private final RegistryKey<World>[] REGISTRY = new RegistryKey[Glyph.ALPHABET_LENGTH];
    private final Object2CharMap<RegistryKey<World>> REVERSE = new Object2CharArrayMap<>();

    private static GlyphOriginRegistry instance;

    public static GlyphOriginRegistry get() {
        return instance;
    }

    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(instance = new GlyphOriginRegistry());
    }

    private GlyphOriginRegistry() {
        REVERSE.defaultReturnValue('*');
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        register(World.OVERWORLD, 'Q');

        for (Identifier id : manager.findResources("poi", f -> f.getPath().endsWith(".json")).keySet()) {
            try (InputStream stream = manager.getResourceOrThrow(id).getInputStream()) {
                char created = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject()
                        .getAsJsonPrimitive("glyph").getAsString().charAt(0);

                this.register(RegistryKey.of(RegistryKeys.WORLD, id), created);
            } catch (Exception e) {
                AmbleKit.LOGGER.error("Error occurred while loading resource json {}", id.toString(), e);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    public void register(RegistryKey<World> key, char c) {
        REGISTRY[Glyph.charToIdx(c)] = key;
        REVERSE.put(key, c);
    }

    public RegistryKey<World> dimForGlyph(char c) {
        return REGISTRY[Glyph.charToIdx(c)];
    }

    public char glyphForDim(RegistryKey<World> key) {
        return REVERSE.computeIfAbsent(key, this::allocate);
    }

    private char allocate(RegistryKey<World> key) {
        for (int i = 0; i < REGISTRY.length; i++) {
            if (REGISTRY[i] == null) {
                char c = Glyph.idxToChar(i);
                register(key, c);

                return c;
            }
        }

        return REVERSE.defaultReturnValue();
    }

    @Override
    public Identifier getFabricId() {
        return StargateMod.id("poi");
    }
}
