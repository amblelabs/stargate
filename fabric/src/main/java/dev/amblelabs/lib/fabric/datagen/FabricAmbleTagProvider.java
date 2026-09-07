package dev.amblelabs.lib.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.impl.datagen.FabricTagBuilder;
import net.fabricmc.fabric.impl.datagen.ForcedTagEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class FabricAmbleTagProvider<T> extends TagsProvider<T> {

    public FabricAmbleTagProvider(FabricDataOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registryKey, registriesFuture);
    }

    protected abstract void addTags(HolderLookup.Provider provider);

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected ResourceKey<T> reverseLookup(T element) {
        Registry registry = BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey);

        if (registry != null) {
            Optional<Holder<T>> key = registry.getResourceKey(element);

            if (key.isPresent()) {
                return (ResourceKey<T>) key.get();
            }
        }

        throw new UnsupportedOperationException("Adding objects is not supported by " + getClass());
    }

    protected AmbleTagBuilder getOrCreateTagBuilder(TagKey<T> tag) {
        return new AmbleTagBuilder(super.tag(tag));
    }
    
    public abstract static class BlockTagProvider extends FabricAmbleTagProvider<Block> {
        public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.BLOCK, registriesFuture);
        }

        @Override
        protected ResourceKey<Block> reverseLookup(Block element) {
            return element.builtInRegistryHolder().key();
        }
    }

    public abstract static class BlockEntityTypeTagProvider extends FabricAmbleTagProvider<BlockEntityType<?>> {
        public BlockEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, Registries.BLOCK_ENTITY_TYPE, completableFuture);
        }

        @Override
        protected ResourceKey<BlockEntityType<?>> reverseLookup(BlockEntityType<?> element) {
            return element.builtInRegistryHolder().key();
        }
    }

    public abstract static class ItemTagProvider extends FabricAmbleTagProvider<Item> {
        @Nullable
        private final Function<TagKey<Block>, TagBuilder> blockTagBuilderProvider;

        public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable FabricAmbleTagProvider.BlockTagProvider blockTagProvider) {
            super(output, Registries.ITEM, completableFuture);

            this.blockTagBuilderProvider = blockTagProvider == null ? null : blockTagProvider::getOrCreateRawBuilder;
        }

        public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            this(output, completableFuture, null);
        }

        public void copy(TagKey<Block> blockTag, TagKey<Item> itemTag) {
            TagBuilder blockTagBuilder = Objects.requireNonNull(this.blockTagBuilderProvider, "Pass Block tag provider via constructor to use copy").apply(blockTag);
            TagBuilder itemTagBuilder = this.getOrCreateRawBuilder(itemTag);
            blockTagBuilder.build().forEach(itemTagBuilder::add);
        }

        @Override
        protected ResourceKey<Item> reverseLookup(Item element) {
            return element.builtInRegistryHolder().key();
        }
    }

    public abstract static class FluidTagProvider extends FabricAmbleTagProvider<Fluid> {
        public FluidTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, Registries.FLUID, completableFuture);
        }

        @Override
        protected ResourceKey<Fluid> reverseLookup(Fluid element) {
            return element.builtInRegistryHolder().key();
        }
    }

    public abstract static class EnchantmentTagProvider extends FabricAmbleTagProvider<Enchantment> {
        public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, Registries.ENCHANTMENT, completableFuture);
        }
    }

    public abstract static class EntityTypeTagProvider extends FabricAmbleTagProvider<EntityType<?>> {
        public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, Registries.ENTITY_TYPE, completableFuture);
        }

        @Override
        protected ResourceKey<EntityType<?>> reverseLookup(EntityType<?> element) {
            return element.builtInRegistryHolder().key();
        }
    }

    public final class AmbleTagBuilder extends TagsProvider.TagAppender<T> {
        private final TagsProvider.TagAppender<T> parent;

        private AmbleTagBuilder(TagsProvider.TagAppender<T> parent) {
            super(parent.builder);
            this.parent = parent;
        }

        /**
         * Set the value of the `replace` flag in a Tag.
         *
         * <p>When set to true the tag will replace any existing tag entries.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder setReplace(boolean replace) {
            ((FabricTagBuilder) this.builder).fabric_setReplace(replace);
            return this;
        }

        /**
         * Add an element to the tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder add(T element) {
            this.add(FabricAmbleTagProvider.this.reverseLookup(element));
            return this;
        }

        /**
         * Add multiple elements to the tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        @SafeVarargs
        public final AmbleTagBuilder add(T... element) {
            Stream.of(element).map(FabricAmbleTagProvider.this::reverseLookup).forEach(this::add);
            return this;
        }

        /**
         * Add an element to the tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder add(Supplier<? extends T> element) {
            return this.add(element.get());
        }

        /**
         * Add multiple elements to the tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder add(Supplier<? extends T>... element) {
            Stream.of(element).map(Supplier::get).forEach(this::add);
            return this;
        }

        /**
         * Add an element to the tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         * @see #add(ResourceLocation)
         */
        @Override
        public AmbleTagBuilder add(ResourceKey<T> registryKey) {
            parent.add(registryKey);
            return this;
        }

        /**
         * Add a single element to the tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder add(ResourceLocation id) {
            builder.addElement(id);
            return this;
        }

        /**
         * Add an optional {@link ResourceLocation} to the tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        @Override
        public AmbleTagBuilder addOptional(ResourceLocation id) {
            parent.addOptional(id);
            return this;
        }

        /**
         * Add an optional {@link ResourceKey} to the tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder addOptional(ResourceKey<? extends T> registryKey) {
            return addOptional(registryKey.location());
        }

        /**
         * Add another tag to this tag.
         *
         * <p><b>Note:</b> any vanilla tags can be added to the builder,
         * but other tags can only be added if it has a builder registered in the same provider.
         *
         * <p>Use {@link #forceAddTag(TagKey)} to force add any tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         * @see BlockTags
         * @see EntityTypeTags
         * @see FluidTags
         * @see GameEventTags
         * @see ItemTags
         */
        @Override
        public AmbleTagBuilder addTag(TagKey<T> tag) {
            builder.addTag(tag.location());
            return this;
        }

        /**
         * Add another optional tag to this tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        @Override
        public AmbleTagBuilder addOptionalTag(ResourceLocation id) {
            parent.addOptionalTag(id);
            return this;
        }

        /**
         * Add another optional tag to this tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder addOptionalTag(TagKey<T> tag) {
            return addOptionalTag(tag.location());
        }

        /**
         * Add another tag to this tag, ignoring any warning.
         *
         * <p><b>Note:</b> only use this method if you sure that the tag will be always available at runtime.
         * If not, use {@link #addOptionalTag(ResourceLocation)} instead.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder forceAddTag(TagKey<T> tag) {
            builder.add(new ForcedTagEntry(TagEntry.element(tag.location())));
            return this;
        }

        /**
         * Add multiple elements to this tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        public AmbleTagBuilder add(ResourceLocation... ids) {
            for (ResourceLocation id : ids) {
                add(id);
            }

            return this;
        }

        /**
         * Add multiple elements to this tag.
         *
         * @return the {@link AmbleTagBuilder} instance
         */
        @SafeVarargs
        @Override
        public final AmbleTagBuilder add(ResourceKey<T>... Registries) {
            for (ResourceKey<T> registryKey : Registries) {
                add(registryKey);
            }

            return this;
        }
    }
}
