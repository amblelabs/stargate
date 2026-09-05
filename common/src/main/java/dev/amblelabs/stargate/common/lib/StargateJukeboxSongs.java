package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;

public class StargateJukeboxSongs {

    public static final ResourceKey<JukeboxSong> THEME_SONG = song("stargate_theme");

    private static ResourceKey<JukeboxSong> song(ResourceLocation id) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, id);
    }

    private static ResourceKey<JukeboxSong> song(String name) {
        return song(StargateAPI.modLoc(name));
    }
}
