package dev.amble.stargate.api.v4.state;

/**
 * A {@link RuntimeException} that gets
 */
public class StateResolveError extends RuntimeException {

    private static final StateResolveError INSTANCE = new StateResolveError();

    private StateResolveError() {
        if (!TStateRegistry.debug)
            this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        if (TStateRegistry.debug) return super.fillInStackTrace();

        this.setStackTrace(new StackTraceElement[0]);
        return this;
    }

    public static StateResolveError create() {
        if (TStateRegistry.debug)
            return new StateResolveError();

        return INSTANCE;
    }
}
