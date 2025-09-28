package dev.amble.stargate.item;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.IrisState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class IrisItem extends Item {

    public IrisItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) return ActionResult.PASS;

        System.out.println("1");
        if (!(context.getWorld().getBlockEntity(context.getBlockPos()) instanceof StargateBlockEntity be) || !be.isLinked())
            return ActionResult.PASS;

        System.out.println("2");
        Stargate stargate = be.gate().get();

        if (stargate.hasState(IrisState.state))
            return ActionResult.PASS;
        System.out.println(3);

        context.getStack().decrement(1);
        stargate.addState(new IrisState());

        System.out.println(4);
        return ActionResult.SUCCESS;
    }
}
