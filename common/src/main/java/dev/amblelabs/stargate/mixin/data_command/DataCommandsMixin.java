package dev.amblelabs.stargate.mixin.data_command;

import com.google.common.collect.ImmutableList;
import dev.amblelabs.stargate.common.commands.StargateDataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(DataCommands.class)
public class DataCommandsMixin {

    @Mutable
    @Shadow
    @Final
    public static List<Function<String, DataCommands.DataProvider>> ALL_PROVIDERS;

    @Redirect(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/server/commands/data/DataCommands;ALL_PROVIDERS:Ljava/util/List;", opcode = Opcodes.PUTSTATIC))
    private static void getAllProviders(List<Function<String, DataCommands.DataProvider>> value) {
        List<Function<String, DataCommands.DataProvider>> list = new ArrayList<>(value);
        list.add(StargateDataAccessor.PROVIDER);

        ALL_PROVIDERS = ImmutableList.copyOf(list);
    }
}
