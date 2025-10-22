package dev.amble.stargate.api.dhd.control;

import dev.amble.stargate.init.StargateSounds;
import net.minecraft.sound.SoundEvent;

public class Symbol {

    protected char glyph; // a glyph to represent the control

    public Symbol(char glyph) {
        this.setGlyph(glyph);
    }

    public char getGlyph() {
        return glyph;
    }

    public void setGlyph(char glyph) {
        this.glyph = glyph;
    }

    public SoundEvent getSound() {
        return StargateSounds.DHD_PRESS;
    }

    @Override
    public String toString() {
        return "SymbolControl{" + "glyph='" + this.glyph + '\'' + '}';
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
        return glyph;
    }
}