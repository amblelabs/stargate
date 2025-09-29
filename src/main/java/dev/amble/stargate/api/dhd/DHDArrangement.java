package dev.amble.stargate.api.dhd;

import dev.amble.stargate.api.dhd.control.impl.Symbol;
import net.minecraft.entity.EntityDimensions;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DHDArrangement {
    private static List<SymbolArrangement> symbolArrangement = new ArrayList<>();

    public static void reloadArrangement(char[] glyphs) {
        symbolArrangement.clear();
        int n = glyphs.length;

        float innerRadius = 0.3f;
        float outerRadius = 0.45f;
        int innerCount = n / 2;
        int outerCount = n - innerCount;
        int offset = 0;
        int otherOffset = 5;

        // Inner ring
        for (int i = 0; i < innerCount; i++) {
            int shiftedIndex = (i + offset) % innerCount;
            double angle = 2 * Math.PI * shiftedIndex / innerCount;
            float x = (float) (innerRadius * Math.sin(angle));
            float z = (float) (innerRadius * Math.cos(angle));
            symbolArrangement.add(new SymbolArrangement(
                    new Symbol(glyphs[18 + i]),
                    EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(x, 0.4f, z + 0.25f).rotateX((float) (Math.PI / -7)).rotateZ(0)
            ));
        }
        // Outer ring
        for (int i = 0; i < outerCount; i++) {
            int shiftedIndex = (i + otherOffset) % outerCount;
            double angle = 2 * Math.PI * shiftedIndex / outerCount;
            float x = (float) (outerRadius * Math.sin(angle));
            float z = (float) (outerRadius * Math.cos(angle));
            symbolArrangement.add(new SymbolArrangement(
                    new Symbol(glyphs[i]),
                    EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(x, 0.4f, z + 0.25f).rotateX((float) (Math.PI / -7)).rotateZ(0)
            ));
        }
    }

    public static List<SymbolArrangement> getSymbolArrangement() {
        return symbolArrangement;
    }
}
