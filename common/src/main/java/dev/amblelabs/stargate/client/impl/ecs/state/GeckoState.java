package dev.amblelabs.stargate.client.impl.ecs.state;

import com.google.common.base.Suppliers;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.util.NbtUtil;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.ChevronState;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("deprecated")
public class GeckoState implements NbtState<GeckoState> {

    private static final Int2ObjectMap<GeoModel<StargateBlockEntity>> MODEL_CACHE = new Int2ObjectOpenHashMap<>();

    public static final Type<GeckoState> state = new Type<>(StargateAPI.modLoc("gecko"), 0) {
        @Override
        public GeckoState fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            if (nbt.contains("path", CompoundTag.TAG_STRING))
                return new GeckoState(Objects.requireNonNull(NbtUtil.getLoc(nbt, "path")));

            ResourceLocation model = Objects.requireNonNull(NbtUtil.getLoc(nbt, "model"));
            ResourceLocation texture = Objects.requireNonNull(NbtUtil.getLoc(nbt, "texture"));
            ResourceLocation animation = NbtUtil.getLoc(nbt, "animation", model);

            return new GeckoState(model, texture, animation);
        }
    };

    public final ResourceLocation model;
    public final ResourceLocation texture;
    public final ResourceLocation animation;

    public final GeoModel<StargateBlockEntity> geoModel;

    public GeckoState(ResourceLocation loc) {
        this(loc, loc, loc);
    }

    public GeckoState(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        this.model = model;
        this.texture = texture;
        this.animation = animation;

        this.geoModel = MODEL_CACHE.computeIfAbsent(Objects.hash(model, texture, animation), i -> new GeoModel<>() {
            private final ResourceLocation model = GeckoState.this.model.withPath(s -> "geo/" + s + ".geo.json");
            private final ResourceLocation texture = GeckoState.this.texture.withPath(s -> "textures/" + s + ".png");
            private final ResourceLocation animation = GeckoState.this.animation.withPath(s -> "animations/" + s + ".animation.json");

            // TODO: sorting manually sucks ass.
            private final Supplier<List<GeoBone>> lights = Suppliers.memoize(() ->
                    this.getAnimationProcessor().getBone("lights").getChildBones().stream().sorted(Comparator.comparing(GeoBone::getName)).toList());

            @Override
            public ResourceLocation getModelResource(StargateBlockEntity animatable) {
                return model;
            }

            @Override
            public ResourceLocation getTextureResource(StargateBlockEntity animatable) {
                return texture;
            }

            @Override
            public ResourceLocation getAnimationResource(StargateBlockEntity animatable) {
                return animation;
            }

            // TODO: this also sucks ass
            @Override
            public void setCustomAnimations(StargateBlockEntity animatable, long instanceId, AnimationState<StargateBlockEntity> animationState) {
                Stargate stargate = animatable.stargate();
                if (stargate == null) return;

                int chevrons = 0;
                GateState.Closed closed = stargate.stateOrNull(GateState.Closed.state);

                if (closed != null) {
                    chevrons = closed.locked;
                } else {
                    ChevronState chevronState = stargate.stateOrNull(ChevronState.state);

                    if (chevronState != null)
                        chevrons = chevronState.chevrons;
                }

                List<GeoBone> lights = this.lights.get();
                for (int j = 0; j < lights.size(); j++) {
                    lights.get(j).setHidden(j >= chevrons);
                }
            }
        });
    }

    @Override
    public Type<GeckoState> type() {
        return state;
    }

    @Override
    public void toNbt(CompoundTag nbt, NbtSerializer.Context context) {
        nbt.putString("model", this.model.toString());
        nbt.putString("texture", this.texture.toString());

        // yes, this is correct. see deserialization code.
        if (this.model != this.animation)
            nbt.putString("animation", this.animation.toString());
    }
}
