package dev.amblelabs.stargate.client.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.util.NbtUtil;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.drtheo.ecs.state.NbtSerializer;
import dev.drtheo.ecs.state.TState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;

public class GeckoState extends GeoModel<StargateBlockEntity> implements TState<GeckoState>, NbtSerializer {

    public static final TState.Type<GeckoState> state = new NbtBacked<>(StargateAPI.modLoc("gecko"), 0) {
        @Override
        public GeckoState fromNbt(CompoundTag nbt, boolean isClient) {
            if (nbt.contains("path", CompoundTag.TAG_STRING)) {
                ResourceLocation loc = NbtUtil.getLoc(nbt, "path");
                return new GeckoState(loc);
            }

            ResourceLocation model = NbtUtil.getLoc(nbt, "model");
            ResourceLocation texture = NbtUtil.getLoc(nbt, "texture");
            ResourceLocation animation = NbtUtil.getLoc(nbt, "animation");

            return new GeckoState(model, texture, animation);
        }
    };

    public final ResourceLocation model;
    public final ResourceLocation texture;
    public final ResourceLocation animation;

    public GeckoState(ResourceLocation loc) {
        this.model = loc.withPath(s -> "geo/" + s + ".geo.json");
        this.texture = loc.withPath(s -> "textures/" + s + ".png");
        this.animation = loc.withPath(s -> "animations/" + s + ".animation.json");
    }

    public GeckoState(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        this.model = model;
        this.texture = texture;
        this.animation = animation;
    }

    @Override
    public Type<GeckoState> type() {
        return state;
    }

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
    public void toNbt(@NotNull CompoundTag nbt, boolean isClient) {
        nbt.putString("model", this.model.toString());
        nbt.putString("texture", this.texture.toString());
        nbt.putString("animation", this.animation.toString());
    }
}
