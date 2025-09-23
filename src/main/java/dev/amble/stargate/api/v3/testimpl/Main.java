package dev.amble.stargate.api.v3.testimpl;

import dev.amble.stargate.api.v3.BehaviorRegistry;
import dev.amble.stargate.api.v3.GateKernel;
import dev.amble.stargate.api.v3.StateRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final int MS_PER_TICK = 1000 / 20;
    static final float NS_PER_MS = 1_000_000f;
    static final long TICKS = 20 * 60 * 60 * 24;

    public static void main(String[] args) {
        new Scanner(System.in).nextLine();

        for (int i = 0; i < 100_000; i++) {
            Dummy dummy = new Dummy();
            dummy.m();
        }

        System.out.println("Warm-up done.");

        BehaviorRegistry.register(new DestinyBehavior.Closed());
        StateRegistry.register(DestinyState.GROUP);

        GateKernel mainKernel = null;
        List<GateKernel> kernels = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            GateKernel kernel = new GateKernel();
            kernel.addState(new DestinyState.Closed());

            if (i == 0)
                mainKernel = kernel;

            kernels.add(kernel);
        }

        long time = System.nanoTime();

        for (int i = 0; i < TICKS; i++) {
            for (GateKernel kernel : kernels) {
                kernel.tick();
            }
        }

        time = System.nanoTime() - time;
        System.out.println("Ticking for 24 hours took " + (int) (time / NS_PER_MS) + "ms (" + Math.round(time / NS_PER_MS / MS_PER_TICK) + " ticks)");
        System.out.println("1 tick took " + (time / TICKS) + "ns on average");
    }
}
