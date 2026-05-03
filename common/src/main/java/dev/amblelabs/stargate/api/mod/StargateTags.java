package dev.amblelabs.stargate.api.mod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public class StargateTags {
    public static final class Items {

        public static TagKey<Item> create(String name) {
            return create(modLoc(name));
        }

        public static TagKey<Item> create(ResourceLocation id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static final class Blocks {

        @SuppressWarnings("unused")
        public static TagKey<Block> create(String name) {
            return TagKey.create(Registries.BLOCK, modLoc(name));
        }
    }

    public static final class Entities {

        @SuppressWarnings("unused")
        public static TagKey<EntityType<?>> create(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, modLoc(name));
        }
    }
}