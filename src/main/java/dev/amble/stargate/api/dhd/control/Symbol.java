package dev.amble.stargate.api.dhd.control;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.init.StargateSounds;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class Symbol {

    public char glyph; // a glyph to represent the control

    public Symbol(char glyph) {
        this.setGlyph(glyph);
    }

    public char getGlyph() {
        return glyph;
    }

    public void setGlyph(char glyph) {
        this.glyph = glyph;
    }

    public boolean runServer(Stargate stargate, ServerPlayerEntity player, ServerWorld world, BlockPos console,
                             boolean leftClick) {
        GateState.Closed closed = stargate.stateOrNull(GateState.Closed.state);

        if (closed != null) {
            closed.address += glyph;
            stargate.markDirty();
            return true;
        }

        return false;
    }

    public SoundEvent getSound() {
        return StargateSounds.DHD_PRESS;
    }

    @Override
    public String toString() {
        return "SymbolControl{" + "glyph='" + this.glyph + '\'' + '}';
    }

    public boolean canRun(Stargate stargate, ServerPlayerEntity user) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || this.getClass() != o.getClass())
            return false;

        Symbol symbolControl = (Symbol) o;
        return symbolControl.glyph == symbolControl.getGlyph();
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.glyph).hashCode();
    }
}