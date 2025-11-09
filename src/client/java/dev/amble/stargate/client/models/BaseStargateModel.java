package dev.amble.stargate.client.models;

import dev.amble.lib.client.model.BlockEntityModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.entity.AnimationState;

public abstract class BaseStargateModel extends BlockEntityModel {

    public final ModelPart[] chevrons;

    public BaseStargateModel() {
        this.chevrons = this.bakeChevronLights();
    }

    @Override
    public void updateAnimation(AnimationState animationState, Animation animation, float animationProgress) {
        super.updateAnimation(animationState, animation, animationProgress);
    }

    public abstract ModelPart[] bakeChevronLights();
}
