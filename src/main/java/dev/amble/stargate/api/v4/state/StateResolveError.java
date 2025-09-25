package dev.amble.stargate.api.v4.state;

/**
 * A {@link RuntimeException} that gets
 */
public class StateResolveError extends RuntimeException {

    private static final StateResolveError INSTANCE = new StateResolveError();

    private StateResolveError() {
        this.setStackTrace(new StackTraceElement[0]);
    }

    private StateResolveError(TStateContainer container, TState.Type<?> type) {
        super("Couldn't find property " + type.id + " in container " + container);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        if (TStateRegistry.debug) return super.fillInStackTrace();

        this.setStackTrace(new StackTraceElement[0]);
        return this;
    }

    public static StateResolveError create(TStateContainer container, TState.Type<?> type) {
        if (TStateRegistry.debug)
            return new StateResolveError(container, type);

        return INSTANCE;
    }
}
