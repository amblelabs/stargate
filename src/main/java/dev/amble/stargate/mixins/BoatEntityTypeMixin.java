package dev.amble.stargate.mixins;

import dev.amble.stargate.init.StargateBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.IntFunction;

@Mixin(BoatEntity.Type.class)
public class BoatEntityTypeMixin {

    @Shadow @Final @Mutable private static BoatEntity.Type[] field_7724;
    @Mutable @Shadow @Final public static StringIdentifiable.Codec<BoatEntity.Type> CODEC;
    @Mutable @Shadow @Final private static IntFunction<BoatEntity.Type> BY_ID;

    private static final BoatEntity.Type NAQUADAH = stargate$addVariant("NAQUADAH", StargateBlocks.NAQUADAH_BLOCK, "naquadah");

    @Invoker("<init>")
    public static BoatEntity.Type stargate$init(String internalName, int ordinal, Block baseBlock, String name) {
        throw new AssertionError();
    }

    private static BoatEntity.Type stargate$addVariant(String internalName, Block baseBlock, String name) {
        int l = field_7724.length;

        BoatEntity.Type[] variants = new BoatEntity.Type[l + 1];
        System.arraycopy(field_7724, 0, variants, 0, l);
        field_7724 = variants;

        BoatEntity.Type result = variants[l] = stargate$init(internalName, l, baseBlock, name);

        CODEC = StringIdentifiable.createCodec(BoatEntity.Type::values);
        BY_ID = ValueLists.<BoatEntity.Type>createIdToValueFunction(Enum::ordinal, variants, ValueLists.OutOfBoundsHandling.ZERO);

        return result;
    }
}
