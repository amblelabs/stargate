package dev.amblelabs.stargate.client.renderers;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class RenderDeduper {

    private static final Set<BlockEntity> RENDERED = Collections.newSetFromMap(new WeakHashMap<>());

    public static void count(BlockEntity blockEntity) {
        RENDERED.add(blockEntity);
    }

    public static boolean shouldSkipRendering(BlockEntity blockEntity) {
        return RENDERED.contains(blockEntity);
    }

    public static void clear() {
        RENDERED.clear();
    }
}
