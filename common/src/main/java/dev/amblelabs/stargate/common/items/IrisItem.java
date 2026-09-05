package dev.amblelabs.stargate.common.items;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.impl.ecs.state.IrisState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class IrisItem extends Item {

    private final Type type;

    public IrisItem(Type type, Properties properties) {
        super(properties);

        this.type = type;
    }

    public IrisState toState() {
        return type.toState();
    }

    public record Type(ResourceLocation loc, int durability) {

        public static final Type TRINIUM = new Type(StargateAPI.modLoc("trinium"), 3200);
        public static final Type NAQUADAH = new Type(StargateAPI.modLoc("naquadah"), 1600);
        public static final Type NETHERITE = new Type(StargateAPI.modLoc("netherite"), 800);
        public static final Type DIAMOND = new Type(StargateAPI.modLoc("diamond"), 400);
        public static final Type GOLD = new Type(StargateAPI.modLoc("gold"), 5);
        public static final Type IRON = new Type(StargateAPI.modLoc("iron"), 200);

        public static final Type[] ALL = {
                TRINIUM, NAQUADAH, NETHERITE, DIAMOND, GOLD, IRON
        };

        public IrisState toState() {
            return new IrisState(loc, durability);
        }
    }
}
