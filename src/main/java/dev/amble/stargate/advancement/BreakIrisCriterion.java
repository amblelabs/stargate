package dev.amble.stargate.advancement;

import com.google.gson.JsonObject;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.state.IrisTier;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class BreakIrisCriterion extends AbstractCriterion<BreakIrisCriterion.Conditions> {

    static final Identifier ID = StargateMod.id("break_iris");

    public Identifier getId() {
        return ID;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        Identifier irisTier = getTier(jsonObject);
        return new Conditions(lootContextPredicate, irisTier);
    }

    private static Identifier getTier(JsonObject root) {
        return new Identifier(JsonHelper.getString(root, "tier"));
    }

    public void trigger(ServerPlayerEntity player, IrisTier state) {
        this.trigger(player, conditions -> conditions.test(state.getRegistryKey()));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final @Nullable Identifier tier;

        public Conditions(LootContextPredicate player, @Nullable Identifier irisTier) {
            super(BreakIrisCriterion.ID, player);
            this.tier = irisTier;
        }

        public static Conditions create(IrisTier tier) {
            return create(tier.getRegistryKey());
        }

        public static Conditions create(RegistryKey<IrisTier> tier) {
            return new Conditions(LootContextPredicate.EMPTY, tier.getValue());
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);

            if (tier != null)
                jsonObject.addProperty("tier", tier.toString());
            return jsonObject;
        }

        public boolean test(RegistryKey<IrisTier> tier) {
            return this.tier == null || this.tier.equals(tier.getValue());
        }
    }
}
