package dev.amblelabs.stargate.common.items.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record StargateLinkedComponent(UUID stargate) {

    public static final Codec<StargateLinkedComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("Stargate").forGetter(StargateLinkedComponent::stargate)
    ).apply(instance, StargateLinkedComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StargateLinkedComponent> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, StargateLinkedComponent::stargate,
            StargateLinkedComponent::new);
}
