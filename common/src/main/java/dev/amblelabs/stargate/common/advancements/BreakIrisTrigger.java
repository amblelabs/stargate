package dev.amblelabs.stargate.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.amblelabs.stargate.common.impl.ecs.state.IrisState;
import dev.amblelabs.stargate.common.items.IrisItem;
import dev.amblelabs.stargate.common.lib.StargateAdvancementTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class BreakIrisTrigger extends SimpleCriterionTrigger<BreakIrisTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, IrisState state) {
        super.trigger(player, instance -> instance.type.equals(state.type));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceLocation type) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                ResourceLocation.CODEC.fieldOf("iris").forGetter(TriggerInstance::type)
                        )
                        .apply(inst, TriggerInstance::new)
        );

        public static Criterion<?> broken(IrisItem.Type type) {
            return StargateAdvancementTriggers.BREAK_IRIS.get().createCriterion(new TriggerInstance(Optional.empty(), type.loc()));
        }
    }
}