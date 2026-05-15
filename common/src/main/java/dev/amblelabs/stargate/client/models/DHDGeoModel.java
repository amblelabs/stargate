package dev.amblelabs.stargate.client.models;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.blocks.DHDBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DHDGeoModel extends GeoModel<DHDBlockEntity> {

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getModelResource(DHDBlockEntity entity) {
        return StargateAPI.modLoc("geo/block/" + entity.getModelName() + ".geo.json");
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getTextureResource(DHDBlockEntity entity) {
        return StargateAPI.modLoc("textures/block/" + entity.getTextureName() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(DHDBlockEntity entity) {
        return StargateAPI.modLoc("animations/block/" + entity.getModelName() + ".animation.json");
    }
}

