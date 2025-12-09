package dev.amble.stargate.api.address;

import com.google.gson.JsonParser;
import dev.amble.lib.AmbleKit;
import dev.amble.stargate.StargateMod;
import it.unimi.dsi.fastutil.chars.CharObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.Object2CharArrayMap;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;

import java.io.Reader;
import java.util.Arrays;
import java.util.Map;
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
        instance = new GlyphOriginRegistry();
//        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(instance = new GlyphOriginRegistry());
    }

    private GlyphOriginRegistry() {
        this.clear();
        this.register(World.OVERWORLD, '3');
    }

    private void clear() {
        Arrays.fill(REGISTRY, null);
        REVERSE.clear();
        REVERSE.defaultReturnValue('*');
    }

    private static final String SUBPATH = "point_of_origin";

    // TODO: figure out why the fuck this causes a thread lock
    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Identifier, Resource> resources = manager.findResources(SUBPATH, f -> f.getPath().endsWith(".json"));

            //noinspection unchecked
            CharObjectImmutablePair<Identifier>[] pairs = new CharObjectImmutablePair[resources.size()];

            int i = 0;
            for (Identifier id : resources.keySet()) {
                try (Reader reader = manager.getResourceOrThrow(id).getReader()) {
                    char created = JsonParser.parseReader(reader).getAsJsonObject()
                            .getAsJsonPrimitive("glyph").getAsString().charAt(0);

                    Identifier dimId = new Identifier(id.getNamespace(),
                            id.getPath().substring(SUBPATH.length() + 1, // 'point_of_origin/'
                                    id.getPath().length() - 5) // .json
                    );

                    pairs[i++] = new CharObjectImmutablePair<>(created, dimId);
                } catch (Exception e) {
                    AmbleKit.LOGGER.error("Error occurred while loading resource json {}", id.toString(), e);
                }
            }

            return pairs;
        }, prepareExecutor).thenComposeAsync(pairs -> {
            this.clear();

            for (CharObjectImmutablePair<Identifier> pair : pairs) {
                this.register(RegistryKey.of(RegistryKeys.WORLD, pair.second()), pair.firstChar());
            }

            return CompletableFuture.completedFuture(null);
        }, applyExecutor);
    }

    public void register(RegistryKey<World> key, char c) {
        REGISTRY[Glyph.charToIdx(c)] = key;
        REVERSE.put(key, c);
    }

    public void register(RegistryKey<World> key, int idx) {
        REGISTRY[idx] = key;
        REVERSE.put(key, Glyph.idxToChar(idx));
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
        return StargateMod.id(SUBPATH);
    }
}
