package dev.amble.stargate.api.v4.behavior;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation used to do DI (Dependency Injection) for {@link TBehavior}s.
 * Works only on {@code private final} fields in registered {@link TBehavior}s.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Resolve { }
