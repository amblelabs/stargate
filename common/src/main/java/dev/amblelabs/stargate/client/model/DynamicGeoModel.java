package dev.amblelabs.stargate.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.Optional;
import java.util.function.BiConsumer;

public class DynamicGeoModel<T extends GeoAnimatable> extends GeoModel<T> {

    public GeoModel<T> model;

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return model.getAnimationResource(animatable);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return model.getTextureResource(animatable);
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return model.getModelResource(animatable);
    }

    @Override
    public void applyMolangQueries(AnimationState<T> animationState, double animTime) {
        model.applyMolangQueries(animationState, animTime);
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        model.setCustomAnimations(animatable, instanceId, animationState);
    }

    @Override
    public void handleAnimations(T animatable, long instanceId, AnimationState<T> animationState, float partialTick) {
        model.handleAnimations(animatable, instanceId, animationState, partialTick);
    }

    @Override
    public void addAdditionalStateData(T animatable, long instanceId, BiConsumer<DataTicket<T>, T> dataConsumer) {
        super.addAdditionalStateData(animatable, instanceId, dataConsumer);
    }

    @Override
    public AnimationProcessor<T> getAnimationProcessor() {
        return super.getAnimationProcessor();
    }

    @Override
    public @Nullable Animation getAnimation(T animatable, String name) {
        return super.getAnimation(animatable, name);
    }

    @Override
    public Optional<GeoBone> getBone(String name) {
        return super.getBone(name);
    }

    @Override
    public BakedGeoModel getBakedModel(ResourceLocation location) {
        return super.getBakedModel(location);
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture) {
        return super.getRenderType(animatable, texture);
    }

    @Override
    public boolean crashIfBoneMissing() {
        return super.crashIfBoneMissing();
    }

    @Override
    public ResourceLocation[] getAnimationResourceFallbacks(T animatable) {
        return super.getAnimationResourceFallbacks(animatable);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        return super.getTextureResource(animatable, renderer);
    }

    @Override
    public ResourceLocation getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        return super.getModelResource(animatable, renderer);
    }
}
