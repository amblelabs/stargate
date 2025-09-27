package dev.amble.stargate.init;

import com.mojang.serialization.Codec;
import dev.amble.stargate.StargateMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class StargateAttachments {

    // TODO: replace with an achievement
    public static AttachmentType<Boolean> HAS_PASSED_THROUGH_STARGATE;

    public static void init() {
        //noinspection UnstableApiUsage
        HAS_PASSED_THROUGH_STARGATE = AttachmentRegistry.<Boolean>builder().copyOnDeath().persistent(Codec.BOOL.orElse(false))
                .initializer(() -> false).buildAndRegister(StargateMod.id("has_passed_through_stargate"));
    }
}
