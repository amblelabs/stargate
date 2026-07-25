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

        this.geoModel = createModel();
    }

    protected GeoModel<StargateBlockEntity> createModel() {
        return new GeoModel<>() {
            private final ResourceLocation model = GeckoState.this.model.withPath(s -> "geo/" + s + ".geo.json");
            private final ResourceLocation texture = GeckoState.this.texture.withPath(s -> "textures/" + s + ".png");
            private final ResourceLocation animation = GeckoState.this.animation.withPath(s -> "animations/" + s + ".animation.json");

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
        };
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

    public static class Default extends GeckoState {

        private static final ResourceLocation MODEL = StargateAPI.modLoc("block/stargate");

        public static final Type<GeckoState> state = new GroupedType<>(GeckoState.state, StargateAPI.modLoc("gecko/default"), 0) {

            @Override
            public GeckoState fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
                ResourceLocation texture = Objects.requireNonNull(NbtUtil.getLoc(nbt, "texture"));

                return new Default(texture);
            }
        };

        public Default(ResourceLocation texture) {
            super(MODEL, texture, MODEL);
        }

        @Override
        protected GeoModel<StargateBlockEntity> createModel() {
            return new GeoModel<>() {
                private final ResourceLocation model = Default.this.model.withPath(s -> "geo/" + s + ".geo.json");
                private final ResourceLocation texture = Default.this.texture.withPath(s -> "textures/" + s + ".png");
                private final ResourceLocation animation = Default.this.animation.withPath(s -> "animations/" + s + ".animation.json");

                private final Supplier<List<GeoBone>> lights = Suppliers.memoize(() ->
                        this.getAnimationProcessor().getBone("lights").getChildBones()
                                .stream().sorted(Comparator.comparing(GeoBone::getName)).toList());

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

                @Override
                public void setCustomAnimations(StargateBlockEntity animatable, long instanceId, AnimationState<StargateBlockEntity> animationState) {
                    Stargate stargate = animatable.stargate();
                    if (stargate == null) return;

                    int chevrons = 0;
                    GateState<?> state = stargate.stateOrNull(GateState.state);

                    if (state instanceof GateState.Closed closed) {
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
            };
        }

        @Override
        public Type<GeckoState> type() {
            return state;
        }
    }
}
