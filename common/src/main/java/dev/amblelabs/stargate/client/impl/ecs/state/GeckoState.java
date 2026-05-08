package dev.amblelabs.stargate.client.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.util.NbtUtil;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.drtheo.ecs.state.NbtSerializer;
import dev.drtheo.ecs.state.TState;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import java.util.Objects;

public class GeckoState implements TState<GeckoState>, NbtSerializer {

    private static final Int2ObjectMap<GeoModel<StargateBlockEntity>> MODEL_CACHE = new Int2ObjectOpenHashMap<>();

    public static final TState.Type<GeckoState> state = new NbtBacked<>(StargateAPI.modLoc("gecko"), 0) {
        @Override
        public GeckoState fromNbt(CompoundTag nbt, boolean isClient) {
            if (nbt.contains("path", CompoundTag.TAG_STRING))
                return new GeckoState(NbtUtil.getLoc(nbt, "path"));

            ResourceLocation model = NbtUtil.getLoc(nbt, "model");
            ResourceLocation texture = NbtUtil.getLoc(nbt, "texture");
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
        });
    }

    @Override
    public Type<GeckoState> type() {
        return state;
    }

    @Override
    public void toNbt(CompoundTag nbt, boolean isClient) {
        nbt.putString("model", this.model.toString());
        nbt.putString("texture", this.texture.toString());

        // yes, this is correct. see deserialization code.
        if (this.model != this.animation)
            nbt.putString("animation", this.animation.toString());
    }
}
