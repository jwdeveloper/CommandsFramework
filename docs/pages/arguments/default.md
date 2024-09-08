# Default Argument types

#### Text

Accept any text value, if you want to include more then 1 word
you need to wrap it around `'` or `"`

> "Hello world"
>
> Hello
>
> 'Hello world'

```java
    commands.create("hello").addTextArgument("arg1").register();
```

#### Number

Accept the number values and converts it into the Double
> 1.23
>
> 1
>
> 1,23

```java
    commands.create("hello").addNumberArgument("arg1").register();
```

#### Bool

Accept the boolean values `true`/`false` but also `1` or `0` 
> true
>
> TRUE
>
> 0
```java
    commands.create("hello").addBoolArgument("arg1").register();
```

#### Player

Accept the name or UUID of the server player.
As suggestions, it returns list of the online players
> Notch
> 5651950a-5cf9-4f63-a8cc-e5adda1de455
```java
    commands.create("hello").addPlayerArgument("arg1").register();
```
#### Location

Accept the x, y, z coordinates of the location
If sender is of type Entity then location will take the sender's world
> 1.2 3 13
```java
    commands.create("hello").addLocationArgument("arg1").register();
```

#### Color

Accept the x, y, z coordinates of the location
If sender is of type Entity then location will take the sender's world
> 1.2 3 13
```java
    commands.create("hello").addColorArugment("arg1").register();
```

#### Entity Type

As a suggestions returns the Entity type enum values
> COW
> 
> CHICKEN
> 
> CREEPER
```java
    commands.create("hello").addEntityArgument("arg1").register();
```

#### Sound

As a suggestions returns the Sound type enum values
>
```java
    commands.create("hello").addSoundArgument("arg1").register();
```

#### Color

As a suggestions returns the ChatColor type enum values
`>
> RED
> 
> GREEN

```java
    commands.create("hello").addColorArgument("arg1").register();
```

#### Particle

As a suggestions returns the Particle type enum values
>
```java
    commands.create("hello").addParticleArgument("arg1").register();
```

#### Material

As a suggestions returns the Particle type enum values
>
```java
    commands.create("hello").addMaterialArgument("arg1").register();
```
