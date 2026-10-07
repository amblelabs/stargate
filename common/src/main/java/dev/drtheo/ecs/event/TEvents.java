package dev.drtheo.ecs.event;

import com.google.common.reflect.AbstractInvocationHandler;
import dev.drtheo.ecs.behavior.TBehavior;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Base interface for all event groups.
 *
 * @author DrTheodor (DrTheo_)
 */
public interface TEvents {

    /**
     * Handles an event without any return type.
     *
     * @param event the event to handle.
     * @param <T> the event group.
     */
    static <T extends TEvents> void handle(TEvent<T> event) {
        event.type().handle(event);
    }

    /**
     * Handles a {@link TEvent.Result} event.
     *
     * @param event the event to handle.
     * @return the result value from the event.
     * @param <T> the event group.
     * @param <R> the event result type.
     */
    static <T extends TEvents, R> @UnknownNullability R handle(TEvent.Result<T, R> event) {
        return event.type().handle(event);
    }

    /**
     * Base interface for all event group types.
     *
     * @param <T> the event group.
     * @author DrTheodor (DrTheo_)
     */
    interface Type<T extends TEvents> {
        /**
         * Handles whether the {@link TBehavior} is applicable for this event type, in which case the behavior gets subscribed to the event.
         *
         * @param behavior the behavior to check.
         * @return whether the behavior is applicable for this event type.
         */
        @Contract(pure = true)
        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        boolean isApplicable(Object behavior);

        /**
         * Subscribes the {@link TBehavior} to the event.
         *
         * @param handler the handler to subscribe.
         */
        void subscribe(T handler);
    }

    /**
     * A basic implementation of the {@link Type} interface for easier and more convenient usage.
     *
     * @param clazz the class of the event group.
     * @param handlers a list of subscribed event handlers.
     * @param <T> the event group.
     * @author DrTheodor (DrTheo_)
     */
    record EventGroup<T extends TEvents>(Class<T> clazz, Deque<T> handlers) implements Type<T> {

        /**
         * A helper constructor for the object, initiates with an empty {@link ArrayDeque} for the handlers.
         *
         * @param clazz the class of the event group.
         */
        public EventGroup(Class<T> clazz) {
            this(clazz, new ArrayDeque<>());
            TEventsRegistry.register(this);
        }

        @SuppressWarnings("unchecked")
        public void subscribe(TBehavior behavior) {
            if (!this.isApplicable(behavior))
                throw new IllegalArgumentException("you're crazy");

            this.subscribe((T) behavior);
        }

        @Override
        public void subscribe(T handler) {
            handlers.add(handler);
        }

        @Override
        @Contract(pure = true)
        public boolean isApplicable(Object behavior) {
            return clazz.isInstance(behavior);
        }

        /**
         * Handles an event without any return type.
         *
         * @param event the event to handle.
         */
        public void handle(TEvent<T> event) {
            event.handleAll(handlers);
        }

        public void notify(Consumer<T> handler) {
            this.handle(new TEvent.Notifier<>(this, handler));
        }

        /**
         * Handles a {@link TEvent.Result} event.
         *
         * @param event the event to handle.
         * @return the result value from the event.
         * @param <R> the event result type.
         */
        public <R> @UnknownNullability R handle(TEvent.Result<T, R> event) {
            this.handle((TEvent<T>) event);
            return event.result();
        }
    }

    final class EventSingle<T extends TEvents> implements Type<T> {
        private final Class<T> clazz;
        private final EventInvoker<T> invoker;

        EventSingle(Class<T> clazz, EventInvoker<T> invoker) {
            this.clazz = clazz;
            this.invoker = invoker;

            TEventsRegistry.register(this);
        }

        interface EventInvoker<T> {
            void subscribe(T handler);

            T invoker();
        }

        static abstract class DequeEventInvoker<T> implements EventInvoker<T> {

            protected final Deque<T> handlers = new ArrayDeque<>();

            @Override
            public void subscribe(T handler) {
                handlers.add(handler);
            }
        }

        static class ProvidedEventInvoker<T> extends DequeEventInvoker<T> {

            private final Function<Iterable<T>, T> func;
            private @Nullable T cache;

            public ProvidedEventInvoker(Function<Iterable<T>, T> func) {
                this.func = func;
            }

            @Override
            public void subscribe(T handler) {
                super.subscribe(handler);

                if (this.cache != null)
                    this.cache = null;
            }

            @Override
            public T invoker() {
                return this.cache != null ? this.cache : (this.cache = this.func.apply(this.handlers));
            }
        }

        static class GeneratedEventInvoker<T> extends DequeEventInvoker<T> {

            private static final MethodType SPREAD_TYPE = MethodType.methodType(Object.class, Object[].class);

            private final Class<T> clazz;
            private final MethodHandle handle;
            private final int paramCount;

            private @Nullable T cache;

            public GeneratedEventInvoker(Class<T> clazz) {
                this.clazz = clazz;

                Method method = findSAM(clazz);

                try {
                    this.handle = MethodHandles.lookup().unreflect(method);
                    this.paramCount = method.getParameterCount();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void subscribe(T handler) {
                super.subscribe(handler);

                if (this.cache != null)
                    this.cache = null;
            }

            @Override
            public T invoker() {
                if (this.cache != null)
                    return this.cache;

                MethodHandle[] dispatch = new MethodHandle[this.handlers.size()];

                for (int i = 0; i < this.handlers.size(); i++) {
                    try {
                        dispatch[i] = handle.bindTo(dispatch[i])
                                .asSpreader(Object[].class, this.paramCount)
                                .asType(SPREAD_TYPE);
                    } catch (Throwable t) {
                        throw new IllegalArgumentException("Cannot bind " + dispatch[i], t);
                    }
                }

                //noinspection unchecked
                return this.cache = (T) Proxy.newProxyInstance(
                        clazz.getClassLoader(), new Class<?>[]{clazz}, new AbstractInvocationHandler() {
                            @Override
                            protected @Nullable Object handleInvocation(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
                                for (MethodHandle mh : dispatch) {
                                    mh.invokeExact(args);
                                }

                                return null;
                            }
                        });
            }

            // SAM for single abstract method
            private static Method findSAM(Class<?> clazz) throws IllegalArgumentException {
                if (!clazz.isInterface())
                    throw new IllegalArgumentException("Not an interface: " + clazz);

                Method sam = null;
                for (Method m : clazz.getMethods()) {
                    if (!Modifier.isAbstract(m.getModifiers())) continue;
                    if (m.getDeclaringClass() == Object.class || m.isSynthetic()) continue;

                    if (sam != null)
                        throw new IllegalArgumentException("Not a functional interface: " + clazz);

                    sam = m;
                }

                if (sam == null)
                    throw new IllegalArgumentException("Not a functional interface: " + clazz);

                return sam;
            }
        }

        /**
         * A helper constructor for the object, initiates with an empty {@link ArrayDeque} for the handlers.
         *
         * @param clazz the class of the event group.
         */
        public EventSingle(Class<T> clazz, Function<Iterable<T>, T> f) {
            this(clazz, new ProvidedEventInvoker<>(f));
        }

        public EventSingle(Class<T> clazz) {
            this(clazz, new GeneratedEventInvoker<>(clazz));
        }

        @Override
        public boolean isApplicable(Object behavior) {
            return clazz.isInstance(behavior);
        }

        @Override
        public void subscribe(T handler) {
            invoker.subscribe(handler);
        }

        public T invoker() {
            return invoker.invoker();
        }

        public Class<T> clazz() {
            return clazz;
        }
    }
}