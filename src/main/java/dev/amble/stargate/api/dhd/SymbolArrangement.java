package dev.amble.stargate.api.dhd;

import dev.amble.stargate.api.dhd.control.Symbol;
import dev.amble.stargate.block.DHDBlock;
import dev.amble.stargate.entities.DHDControlEntity;
import net.minecraft.entity.EntityDimensions;
import org.joml.Vector3f;

/**
 * Holds a control which will be run when interacted with, an
 * {@link Vector3f offset} from the centre of the {@link DHDBlock} and
 * a {@link EntityDimensions scale} for the entity <br>
 * <br>
 * A list of these is gotten by {@link DHDArrangement#getSymbolArrangement()} and
 * used in {@link DHDControlEntity} to hold its information
 *
 * @author loqor
 * @see DHDControlEntity
 */
public class SymbolArrangement {
    private final Symbol symbolControl;
    private final EntityDimensions scale;
    private final Vector3f offset;

    public SymbolArrangement(Symbol symbolControl, EntityDimensions scaling, Vector3f offset) {
        this.symbolControl = symbolControl;
        this.scale = scaling;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "ControlTypes{" + "control=" + symbolControl + ", scale=" + scale + ", offset=" + offset + '}';
    }

    public Symbol getControl() {
        return this.symbolControl;
    }

    public EntityDimensions getScale() {
        return this.scale;
    }

    public Vector3f getOffset() {
        return this.offset;
    }
}
