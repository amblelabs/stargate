package dev.amble.stargate.client.animations;// Save this class in your mod and generate all required imports

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * @author Loqor
 */
public class StargateAnimations {
	public static final Animation IRIS_CLOSE = Animation.Builder.create(3.0F)
			.addBoneAnimation("blade1", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade1", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade5", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade5", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade9", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade9", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade13", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade13", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade17", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade17", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade3", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade3", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade7", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade7", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade11", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade11", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade15", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade15", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade2", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade2", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade4", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade4", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade6", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade6", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade8", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade8", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade10", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade10", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade12", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade12", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade14", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade14", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade16", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade16", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade18", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade18", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.build();

	public static final Animation LOCK_SYMBOL = Animation.Builder.create(2.3333F)
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

	public static final Animation IRIS_OPEN = Animation.Builder.create(3.0F)
			.addBoneAnimation("blade1", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade1", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade5", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade5", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade9", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade9", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade13", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade13", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade17", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade17", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade3", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade3", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade7", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade7", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade11", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade11", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade15", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade15", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade2", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade2", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade4", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade4", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade6", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade6", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade8", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade8", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade10", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade10", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade12", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade12", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade14", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade14", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade16", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade16", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade18", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -67.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("blade18", new Transformation(Transformation.Targets.SCALE,
					new Keyframe(2.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(3.0F, AnimationHelper.createScalingVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.build();
}