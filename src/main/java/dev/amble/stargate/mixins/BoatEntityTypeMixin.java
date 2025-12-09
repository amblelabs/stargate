package dev.amble.stargate.mixins;

import dev.amble.modkit.api.BoatTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.function.IntFunction;

@Mixin(BoatEntity.Type.class)
public class BoatEntityTypeMixin implements BoatTypeRegistry.CustomBoatTypes {

    @Mutable @Shadow @Final private static BoatEntity.Type[] field_7724;
    @Mutable @Shadow @Final public static StringIdentifiable.Codec<BoatEntity.Type> CODEC;
    @Mutable @Shadow @Final private static IntFunction<BoatEntity.Type> BY_ID;

    @Invoker("<init>")
    public static BoatEntity.Type stargate$init(String internalName, int ordinal, Block baseBlock, String name) {
        throw new AssertionError();
    }

    @Unique
    private static BoatEntity.Type stargate$init(BoatTypeRegistry.Entry entry, int offset) {
        return stargate$init(entry.getEnumName(),
                field_7724.length + offset,
                entry.block(), entry.getPath()
        );
    }

    @Override
    public Item[] amble$recalc(List<BoatTypeRegistry.Entry> entries) {
        int l = field_7724.length;

        BoatEntity.Type[] variants = new BoatEntity.Type[l + entries.size()];
        Item[] items = new Item[variants.length];

        System.arraycopy(field_7724, 0, variants, 0, l);

        for (int i = 0; i < l; i++) {
            items[i] = switch (variants[i]) {
                case SPRUCE -> Items.SPRUCE_BOAT;
                case BIRCH -> Items.BIRCH_BOAT;
                case JUNGLE -> Items.JUNGLE_BOAT;
                case ACACIA -> Items.ACACIA_BOAT;
                case CHERRY -> Items.CHERRY_BOAT;
                case DARK_OAK -> Items.DARK_OAK_BOAT;
                case MANGROVE -> Items.MANGROVE_BOAT;
                case BAMBOO -> Items.BAMBOO_RAFT;
                default -> Items.OAK_BOAT;
            };
        }

        for (int i = 0; i < entries.size(); i++) {
            BoatTypeRegistry.Entry entry = entries.get(i);

            items[l + i] = entry.item();
            variants[l + i] = stargate$init(entry, i);
        }

        field_7724 = variants;

        CODEC = StringIdentifiable.createCodec(BoatEntity.Type::values);
        BY_ID = ValueLists.<BoatEntity.Type>createIdToValueFunction(Enum::ordinal, variants, ValueLists.OutOfBoundsHandling.ZERO);

        return items;
    }
}
