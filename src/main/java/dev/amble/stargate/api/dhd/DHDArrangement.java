package dev.amble.stargate.api.dhd;

import dev.amble.stargate.api.address.Glyph;
import dev.amble.stargate.api.address.GlyphOriginRegistry;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DHDArrangement {

    public static final List<SymbolArrangement> SYMBOLS = new ArrayList<>(Glyph.ALL.length);

    private static final float innerRadius = 0.3f;
    private static final float outerRadius = 0.45f;

    private static final int innerCount = Glyph.ALL.length / 2;
    private static final int outerCount = Glyph.ALL.length - innerCount;

    private static final int offset = 0;
    private static final int otherOffset = 5;

    static {
        reloadArrangement(Glyph.ALL);
    }

    public static void reloadArrangement(char[] glyphs) {
        SYMBOLS.clear();

        System.out.println(innerCount);
        System.out.println(outerCount);

        // Inner ring
        for (int i = 0; i < innerCount; i++) {
            int shiftedIndex = (i + offset) % innerCount;
            float angle = 2 * MathHelper.PI * shiftedIndex / innerCount;

            float x = innerRadius * MathHelper.sin(angle);
            float z = innerRadius * MathHelper.cos(angle);

            SYMBOLS.add(new SymbolArrangement(
                    glyphs[18 + i],
                    EntityDimensions.changing(0.1f, 0.1f),
                    SymbolArrangement.Offset.changing(x, 0.4f, z + 0.25f).rotateX((float) (Math.PI / -7)).rotateZ(0)
            ));
        }

        // Outer ring
        for (int i = 0; i < outerCount; i++) {
            int shiftedIndex = (i + otherOffset) % outerCount;
            float angle = 2 * MathHelper.PI * shiftedIndex / outerCount;

            float x = outerRadius * MathHelper.sin(angle);
            float z = outerRadius * MathHelper.cos(angle);

            SYMBOLS.add(new SymbolArrangement(
                    glyphs[i],
                    EntityDimensions.changing(0.1f, 0.1f),
                    SymbolArrangement.Offset.changing(x, 0.4f, z + 0.25f).rotateX((float) (Math.PI / -7)).rotateZ(0)
            ));
        }
    }

    public static SymbolArrangement poi(World world) {
        char poi = GlyphOriginRegistry.get().glyph(world.getRegistryKey());

        return new SymbolArrangement(poi,
                EntityDimensions.fixed(0.2f, 0.2f), SymbolArrangement.Offset.fixed(0, 0.4625f, 0));
    }
}
