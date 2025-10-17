package dev.amble.stargate.client.models;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.render.StargateAnimateEvent;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public abstract class BaseStargateModel extends SinglePartEntityModel {

    public BaseStargateModel() {
        this(RenderLayer::getEntityCutoutNoCull);
    }

    public BaseStargateModel(Function<Identifier, RenderLayer> function) {
        super(function);
    }

    public void animateStargateModel(StargateBlockEntity stargateBlockEntity, Stargate stargate, int age) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        TEvents.handle(new StargateAnimateEvent(stargateBlockEntity, stargate, this, age));
    }

    @Override
    public void updateAnimation(AnimationState animationState, Animation animation, float animationProgress) {
        super.updateAnimation(animationState, animation, animationProgress);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
                          float headPitch) {
    }
}
