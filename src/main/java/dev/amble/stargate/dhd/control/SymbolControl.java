package dev.amble.stargate.dhd.control;

import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.init.StargateSounds;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class SymbolControl {

    public char glyph; // a glyph to represent the control

    public SymbolControl(char glyph) {
        this.setGlyph(glyph);
    }

    public char getGlyph() {
        return glyph;
    }

    public void setGlyph(char glyph) {
        this.glyph = glyph;
    }

    public boolean runServer(Stargate stargate, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (stargate.state() instanceof GateState.Closed closed)
            closed.appendGlyph(this.glyph);
        return false;
    }

    public boolean runServer(Stargate stargate, ServerPlayerEntity player, ServerWorld world, BlockPos console,
                             boolean leftClick) {
        return runServer(stargate, player, world, console);
    }

    public SoundEvent getSound() {
        return StargateSounds.DHD_PRESS;
    }

    @Override
    public String toString() {
        return "SymbolControl{" + "glyph='" + this.glyph + '\'' + '}';
    }

    public boolean canRun(Stargate stargate, ServerPlayerEntity user) {
        return stargate.state() instanceof GateState.Closed closed && closed.isDialing();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || this.getClass() != o.getClass())
            return false;

        SymbolControl symbolControl = (SymbolControl) o;
        return symbolControl.glyph == symbolControl.getGlyph();
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.glyph).hashCode();
    }
}