ECS stands for Entity Component System.


The simplest way you can represent ECS from a normal OOP standpoint is a side-by-side comparison.

It's important to note, that ECS, although greatly benefitting from the ideas of the "OOP actually sucks crowd", actually becomes shiny with inheritance and polymorphism, which are part of OOP.

```java
abstract class Entity implements Thing {
  public abstract void live();
  public abstract void die();
}

class Duck extends Entity {
  Duck(String sound) { /* ... */ }
  void quack() {
    System.out.println(this.sound));
  }
}

class World {

  void continueSpinning() {
    for (Entity entity : this.entities) {
      entity.live();
    }
  }
}

World world = new World();
Duck duck = new Duck("gorge");
world.addEntity(duck);

while (true) {
  world.continueSpinning(); // FIXME: throws exceptions
}
```
The standard OOP way. `Entity`s are a handy class that does all the stuff in the back so when you add a new thing you don't really have to worry about the inner workings. The problem is: the entire system (the `World`) now depends on the `Entity`. 

Let's say we want to add a new `Thing`. Well, easy, new class, `implements Thing`. However, it shares some similarities to entities. For example, in a way that gets processed. But you don't really need all the stuff that comes with it. Nor can it live or die. What to do? The easiest solution would be to just implement those methods and leave them empty. But what if there were methods that return values: e.g. health? 

Another example, this time let's take Minecraft as the base. We have blocks and we have entities. You want to add blockentities. How? You can't extend both classes in one. As a result, you have to copy half of how you handle entities then add your custom blockentity exclusive code, and then strap them to blocks... Yuck! Now everything depends on everything, a bunch of abstract classes...

```java
record EntityUid(int id) { }

World world = new World();
EntityUid duck = new EntityUid(666); // ducks are evil.
world.addEntity(duck);

world.addComponent(duck, new LivingComponent());
world.addComponent(duck, new DuckComponent("quack")); // quacks like a duck!

handleEvent(WorldEvent::continueSpinning);

class DuckBehavior implements System {
  void onWorldSpin(DuckComponent duck) {
    System.out.println(duck.sound());
  }
}
```

First of all, everything is an entity now. And entities aren't abstract classes anymore, they're just a number (wrapped into a `record` type for our convenience). 
Second: there are no hard dependencies on anything. If you tried to unweave the tangle that was the first listing, you'd see that this code only depends on it's own isolated elements and the base underlying architecture:
- `DuckComponent` & `DuckSystem`. 
- `World`, which stores entities and components (but it doesn't know what concrete components there are!).
- The base ECS system (Component, System, event bus, etc)


In ECS, systems do the logic and components store data. This separation of concern allows for more flexibility. "Composition over inheritance" is used here: to make something you add a bunch of various components to it, each doing it's own thing. The glue that most commonly binds it all together is an event bus. The most important difference is that there's only one instance of the system and it doesn't hold any concrete state.

ECS can shine even more in languages that allow you to control your memory allocations and how much space everything actually takes, since you can do some clever tricks that allow you to query all existing components by type with small memory overhead and high reading and writing speeds (O(1)).


It's also important to note that ECS is just a set of various ideas and not a concrete playbook with seemingly unshakeable rules and concepts (like OOP).

## dev.drtheo.ecs & its mc implementation

TECS (yes i will call it that) is practically fine-tuned for Minecraft, yet it doesn't have a hard dependency on it. Components are called states and systems are called behaviors. 
- The design allows for a flexible state holder (`TStateContainer`), that can be backed by anything: hashmaps, arrays, whatever you prefer really. This allows for incredibly fast reading and writing performance at the expense of more memory usage.
- An event bus (also very fast).
- Flexible. Quite a bit.

The basic system is quite barebones and you can't exactly use it in MC right away, so there's an implementation of it for Minecraft. It extends the basic functionality by providing short hands and overrides for TECS that has a hard dependency on Minecraft, which allows to use stuff like `ResourceLocation`s, `CompoundTag`s and etc.

## States

Unlike a normal ECS, where you map components to entities, here, we still have a centerpiece - the object implementing TStateContainer. This object will actually hold the state instances, which you can query by using the appropriate `TState.Type` object.

Example:
```java
class SomeState implements TState {
  // this being public static and being named state is
  //  actually a convention, the idea is that this will look similar to using ".class" on a type!
  public static final Type<SomeState> state = new Type<>(ResourceLocation.fromNamespaceAndPath("modid", "some"));

  @Overridd
  public Type<SomeState> type() {
    return state;
  }
}

// registration here

TStateContainer container = ...;

container.addState(SomeState.state, new SomeState());
```

That's the base idea for state management. For example, in Stargate, the Stargate would be the state container.

To get the state, you can use `#stateOrNull`. Behaviors running methods from events (or when called from outside in a safe way) can use the `#state` to get the state in a more convenient way:
```java
TStateContainer container = ...;
container.state(SomeOtherState.state);
System.out.println("hi?"); // won't run, because container doesn't have SomeOtherState!
```
However:
```java
TStateContainer container = ...;
var other = container.stateOrNull(SomeOtherState.state);
System.out.println("hi?"); // will run, but other will be null
```

## Behaviors

```java
class FooBehavior implements TBehavior {

}

class BarBehavior implements TBehavior {
  @Resolve
  private final FooBehavior foo = behavior();
}

// registration here
```

Behaviors are created just once. If you want to get a behaviour from another behaviour you can just use the `@Resolve` annotation, which will magically put the appropriate behavior's instance in the field.

## Events

```java
interface EntityEvents extends TEvents {
  Type<EntityEvents> type = new Type(EntityEvents.class);

  void event1(); // an event that a subscriber *must* "handle", can be a sign of an event that is called a lot

  default void event2() { } // default impl, the subscriber is allowed to be clueless this time
}

record Event1() implements TEvent.Notify<EntityEvents> { 
  @Overrids
  public BaseType<EntityEvents> type() {
    return EntityEvents.type;
  }

  @Override
  public void handle(EntityEvents handler) {
    handler.event1();
  }
}

// Event2 impl...

TEvents.handle(new Event1());
```

Events in TECS are grouped. Instead of subscribing to concrete events you subscribe to a group of events (a group can consist of just 1 event, too).

In case of a situation where you have an event that gets called a lot and very frequently (e.g. rendering or ticking) and you subscribe to the event group for something else, then this might be an indicator that these events should be ungrouped. 

The base type for events is `TEvent`, but there is a convenient type `TEvent.Notify` for events without results.

```java
class SomeBehavior implements TBehavior, EntityEvents {
  @Override
  public void event1() { }
}
```

To subscribe to an event group, you just need to implement the event group's interface in the behaviour, which will serve as the subscriber.

## Serialization

Serialization is part of the extended API provided by the MC implementation. 

If you want a state to be serializable (a common-side state), then you must use `NbtState` and `NbtState.Type`:
```java
class IrisState implements NbtState<IrisState> {

  public static final Type<IrisState> type = new Type<>(modLoc("iris"), 0) {
    @Override
    public IrisState fromNbt(CompoundTag nbt, NbtDeserializer.Context ctx) {
      return new IrisState(nbt.getString("type"));
    }
  };

  public String type;

  IrisState(String type) {
    this.type = type;
  }

  @Overrise
  public void toNbt(CompoundTag nbt, NbtSerializer.Context ctx) {
    nbt.putString("type", type);
  }

  @Overrids
  public Type<IrisState> type() {
    return type;
  }
}
```

When creating an nbt state type, you provide multiple arguments:
1. The resource location (as always)
2. The version (`0`)
3. Varargs fixes (empty here, since there were no fixes)

You also must implement the `#fromNbt` method.

Fixes allow to implement changes in the structure, allowing to "fix" the data to a new format.

```java
private static final Fix TYPE_TO_LOC_FIX = new Fix(1, tag -> {
    String type = tag.getString("type");
    tag.putString(StargateAPI.MOD_ID + ":" + type);
  }); // this will apply when updating to v1
```

