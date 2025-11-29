package dev.amble.stargate.init;

import dev.amble.lib.advancement.SimpleCriterion;
import dev.amble.stargate.advancement.BreakIrisCriterion;

public class StargateCriterions {

    public static final SimpleCriterion PASSED_THROUGH = SimpleCriterion.create("passed_through").register();
    public static final BreakIrisCriterion IRIS_BREAK = new BreakIrisCriterion();

    public static void init() { }
}
