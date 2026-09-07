package dev.amblelabs.stargate.common.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.util.CustomComponentTagVisitor;
import dev.amblelabs.stargate.common.I18n;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

public class StargateDataAccessor implements DataAccessor {

    public static final SimpleCommandExceptionType NO_STARGATE_FOUND = new SimpleCommandExceptionType(I18n.Commands.Arguments.NOT_FOUND);

    public static final Function<String, DataCommands.DataProvider> PROVIDER = (string) -> new DataCommands.DataProvider() {
        public DataAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            // TODO: implement a custom argument type that accepts both BlockPos AND UUID
            //   UUID id = UuidArgument.getUuid(context, string);
            //   Stargate stargate = StargateNetwork.get(context.getSource().getLevel()).get(id);

            BlockPos blockPos = BlockPosArgument.getBlockPos(context, string + "Pos");
            BlockEntity blockEntity = context.getSource().getLevel().getBlockEntity(blockPos);

            if (!(blockEntity instanceof StargateBlockEntity stargateBlockEntity))
                throw NO_STARGATE_FOUND.create();

            Stargate stargate = stargateBlockEntity.stargate();
            if (stargate == null) throw NO_STARGATE_FOUND.create();

            return new StargateDataAccessor(stargateBlockEntity, stargate);
        }

        public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> builder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> action) {
            return builder.then(Commands.literal("stargate").then(action.apply(Commands.argument(string + "Pos", BlockPosArgument.blockPos()))));
        }
    };

    private final StargateBlockEntity stargateBlockEntity;
    private final Stargate stargate;

    public StargateDataAccessor(StargateBlockEntity stargateBlockEntity, Stargate stargate) {
        this.stargateBlockEntity = stargateBlockEntity;
        this.stargate = stargate;
    }

    @Override
    public void setData(CompoundTag other) {
        other.remove(Stargate.TAG_ID);

        CompoundTag tag = new CompoundTag();
        tag.put(Stargate.TAG_STATES, other);

        this.stargate.fromNbt(tag, NbtDeserializer.Context.forLoad().get());
        this.stargateBlockEntity.setChanged(); // this sends a level update as well
    }

    @Override
    public CompoundTag getData() {
        CompoundTag tag = new CompoundTag();
        this.stargate.toNbt(tag, new NbtSerializer.Context(false));

        tag = tag.getCompound(Stargate.TAG_STATES);
        tag.putUUID(Stargate.TAG_ID, this.stargate.getId());

        return tag;
    }

    @Override
    public Component getModifiedSuccess() {
        BlockPos pos = this.stargateBlockEntity.getBlockPos();
        return I18n.Commands.dataModified(pos);
    }

    @Override
    public Component getPrintSuccess(Tag nbt) {
        BlockPos pos = this.stargateBlockEntity.getBlockPos();
        return I18n.Commands.dataQuery(pos, new CustomComponentTagVisitor("  ") {
            @Override
            public Tag onTag(String key, Tag tag) {
                if (key.equals(Stargate.TAG_ID) && tag.getId() == Tag.TAG_INT_ARRAY)
                    tag = StringTag.valueOf(NbtUtils.loadUUID(tag).toString());

                return tag;
            }
        }.visit(nbt));
    }

    @Override
    public Component getPrintSuccess(NbtPathArgument.NbtPath path, double scale, int value) {
        BlockPos pos = this.stargateBlockEntity.getBlockPos();
        return I18n.Commands.dataGet(path.asString(), pos, scale, value);
    }
}
