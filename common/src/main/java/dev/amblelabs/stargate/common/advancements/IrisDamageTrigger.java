package dev.amblelabs.stargate.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.amblelabs.stargate.common.lib.StargateAdvancementTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class IrisDamageTrigger extends SimpleCriterionTrigger<IrisDamageTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, instance -> instance.died != player.isAlive());
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, boolean died) implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                Codec.BOOL.optionalFieldOf("died", false).forGetter(TriggerInstance::died)
                        )
                        .apply(inst, TriggerInstance::new)
        );

        public static Criterion<?> dead() {
            return StargateAdvancementTriggers.IRIS_DAMAGE.get().createCriterion(new TriggerInstance(Optional.empty(), true));
        }
    }
}