package dev.amblelabs.lib.datagen;

import net.minecraft.advancements.*;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "SameParameterValue", "unused"})
public abstract class AmbleAdvancementSubProvider implements AdvancementSubProvider {

    protected final String modId;

    protected AmbleAdvancementSubProvider(String modId) {
        this.modId = modId;
    }

    protected ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(modId, name);
    }

    public Builder create(AdvancementHolder parent, String name) {
        return new Builder(parent, name);
    }

    public Builder create(String name) {
        return create(null, name);
    }

    public Builder task(AdvancementHolder parent, String name) {
        return create(parent, name);
    }

    public Builder task(String name) {
        return create(name);
    }

    public Builder challenge(AdvancementHolder parent, String name) {
        return create(parent, name).frame(AdvancementType.CHALLENGE);
    }

    public Builder goal(AdvancementHolder parent, String name) {
        return create(parent, name).frame(AdvancementType.GOAL);
    }

    public class Builder {

        private final Advancement.Builder builder;

        private ItemLike item = Items.BARRIER;
        private boolean hidden = false;
        private AdvancementType frame = AdvancementType.TASK;
        private ResourceLocation background;
        private boolean announce = true;
        private boolean showToast = true;

        private final String name;

        public Builder(@Nullable AdvancementHolder parent, String name) {
            this.builder = Advancement.Builder.advancement();

            if (parent != null)
                this.builder.parent(parent);

            this.name = name;
        }

        public Builder condition(String name, Criterion<?> conditions) {
            this.builder.addCriterion(name, conditions);
            return this;
        }

        public Builder icon(ItemLike item) {
            this.item = item;
            return this;
        }

        public Builder hidden() {
            this.hidden = true;
            return this;
        }

        public Builder frame(AdvancementType frame) {
            this.frame = frame;
            return this;
        }

        public Builder background(ResourceLocation background) {
            this.background = background;
            return this;
        }

        public Builder background(String background) {
            return background(modLoc(background));
        }

        public Builder silent() {
            this.announce = false;
            return this;
        }

        public Builder noToast() {
            this.showToast = false;
            return this;
        }

        public AdvancementHolder build(Consumer<AdvancementHolder> consumer) {
            String modId = AmbleAdvancementSubProvider.this.modId;
            String finalName = "advancement." + modId + "." + name;

            AdvancementHolder holder = builder
                    .display(item,
                            Component.translatable(finalName),
                            Component.translatable(finalName + ".desc"),
                            background, frame, showToast, announce, hidden)
                    .build(modLoc(name));

            consumer.accept(holder);
            return holder;
        }
    }
}