package dev.amble.stargate.item;

import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.item.ModonomiconCustomItemBase;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.StargateMod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class GuideBookItem extends ModonomiconCustomItemBase {

    public static final Identifier BOOK = StargateMod.id("stellar_map");

    public GuideBookItem(AItemSettings properties) {
        super(BOOK, properties);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        NbtCompound cmp = stack.getOrCreateNbt();

        cmp.putString(ModonomiconConstants.Nbt.ITEM_BOOK_ID_TAG, BOOK.toString());
        return stack;
    }
}