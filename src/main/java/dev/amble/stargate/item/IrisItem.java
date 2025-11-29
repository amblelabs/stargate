package dev.amble.stargate.item;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.IrisState;
import dev.amble.stargate.api.state.IrisTier;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateIrisTiers;
import dev.amble.stargate.init.StargateItems;
import dev.amble.stargate.util.NbtUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class IrisItem extends Item {

    private final IrisTier tier;

    public IrisItem(IrisTier tier, Settings settings) {
        super(settings);

        this.tier = tier;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) return ActionResult.PASS;

        if (!(context.getWorld().getBlockEntity(context.getBlockPos()) instanceof StargateBlockEntity be) || !be.isLinked())
            return ActionResult.PASS;

        Stargate stargate = be.asGate();

        if (stargate.hasState(IrisState.state))
            return ActionResult.PASS;

        context.getStack().decrement(1);
        stargate.addState(new IrisState(tier));

        return ActionResult.SUCCESS;
    }
}
