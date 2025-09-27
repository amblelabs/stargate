package dev.amble.stargate.dhd.control;

import dev.amble.stargate.api.v3.Stargate;
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
        if (stargate.state() instanceof GateState.Closed closed) {
            if (this.getGlyph() != '*' && !(closed.locked() > 6)) {
                if (!closed.contains(this.glyph)) {
                    closed.setHasDialButton(false);
                    closed.appendGlyph(this.glyph);
                    closed.lock();
                }
            } else {
                closed.setHasDialButton(true);
            }
            stargate.markDirty();
        } else if (stargate.state() instanceof GateState.Open open) {
            System.out.println("HELLO?");
            if (this.getGlyph() == '*') {
                stargate.kernel().setState(new GateState.Closed());
                stargate.markDirty();
                Stargate gate = open.target().get();

                gate.kernel().setState(new GateState.Closed());
                gate.markDirty();
            }
        }
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
        return stargate.state() instanceof GateState.Closed || stargate.state() instanceof GateState.Open;
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