package dev.amble.stargate.client.animations;// Save this class in your mod and generate all required imports

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * @author Loqor
 */
public class StargateAnimations {
	public static final Animation IRIS_CLOSE = Animation.Builder.create(3.5F)
		.addBoneAnimation("iris", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 360.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade4", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.25F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.9167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.4167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade5", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.3333F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade6", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.4167F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade7", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.1667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade8", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.5833F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.25F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.75F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade9", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.6667F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.8333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade10", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.75F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.4167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade11", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.8333F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade12", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.9167F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade13", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(1.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.1667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade14", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(1.0833F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.75F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.25F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade15", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(1.1667F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.8333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.3333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade16", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(1.25F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade17", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(1.3333F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade18", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(1.4167F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("undefined", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.25F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.9167F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade3", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.1667F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.8333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade2", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0833F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.75F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.25F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.1667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation LOCK_SYMBOL = Animation.Builder.create(2.3333F).looping()
		.addBoneAnimation("chev7", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("chev_light7", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("chev7bottom", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 1.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 1.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("chev_light7bottom", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 1.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 1.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation IRIS_OPEN = Animation.Builder.create(3.5F)
		.addBoneAnimation("iris", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 360.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade4", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.75F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade5", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.6667F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade6", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.4167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5833F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade7", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.8333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade8", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.25F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.75F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4167F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade9", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.1667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.3333F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade10", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.25F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade11", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.1667F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade12", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.9167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.4167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade13", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.8333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade14", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.75F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.25F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9167F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade15", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.1667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.8333F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade16", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0833F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.75F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade17", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade18", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.9167F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5833F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("undefined", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(2.0833F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.75F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade3", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.1667F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.8333F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade2", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.75F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.25F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("blade", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.8333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.3333F, AnimationHelper.createScalingVector(0.8F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
		))
		.build();
}