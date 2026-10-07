1. Use `@Resolve` where applicable.
2. Process everything in events where applicable (otherwise use `TEvent#handleSilent`).
3. Event groups must use `type` and single events must use `event`.
4. Name the type field `state` for `TState`.
5. Use `@FunctionalInterface` on single events.
6. Make a `static void call` method for `Notify` events in the appropriate `TEvents` if it's a single event.