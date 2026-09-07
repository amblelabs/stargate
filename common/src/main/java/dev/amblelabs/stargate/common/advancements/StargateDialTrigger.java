package dev.amblelabs.stargate.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.amblelabs.stargate.common.lib.StargateAdvancementTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class StargateDialTrigger extends SimpleCriterionTrigger<StargateDialTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, int chevrons) {
        super.trigger(player, instance -> instance.chevrons.matches(chevrons));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints chevrons) implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                MinMaxBounds.Ints.CODEC.fieldOf("chevrons").forGetter(TriggerInstance::chevrons)
                        )
                        .apply(inst, TriggerInstance::new)
        );

        public static Criterion<?> dialed(MinMaxBounds.Ints chevrons) {
            return StargateAdvancementTriggers.DIAL.get().createCriterion(new TriggerInstance(Optional.empty(), chevrons));
        }
    }
}