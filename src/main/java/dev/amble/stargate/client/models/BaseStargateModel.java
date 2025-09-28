package dev.amble.stargate.client.models;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.client.ClientIrisState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.animations.StargateAnimations;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
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

        this.updateAnimation(stargateBlockEntity.CHEVRON_LOCK_STATE, StargateAnimations.LOCK_SYMBOL, age);

        ClientIrisState clientIrisState = stargate.stateOrNull(ClientIrisState.state);

        if (clientIrisState != null) {
            this.updateAnimation(clientIrisState.CLOSE_STATE, StargateAnimations.IRIS_CLOSE, age);
            this.updateAnimation(clientIrisState.OPEN_STATE, StargateAnimations.IRIS_OPEN, age);
        }
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
                          float headPitch) {
    }
}
