# Annotations

### What is an annotation?

Annotation is kind of `"tag"` that you can
place over the `class` or `method`. With it, you
can configure a command or argument.
Currently, there are 2 annotations available

- FCommand
- FArgument

---

### @FCommand

#### Basic example

Use `@FCommand` annotation over a method or class.
In this annotation you can define what command pattern and adjust other properties.

In this example as you can see we have 2 entry points for the command.
One is for a `Player` and another for `ConsoleCommandSender`.

There could be as many entry point as you need, however remember
that the command configuration is global for all entry point.
That means is enough to set `description` over  `helloForPlayer` method,
and you don't need to repeat this again over `helloForConsole`

```java
public final class MyHelloCommand {

    @FCommand(name = "hello", permission = "world.commands.spawn")
    @FArgument(name = "name")
    @FArgument(name = "age", type = "Number")
    public void helloForPlayer(Player sender, String name, int age) {
        event.sender().sendMessage("Name: " + name + " Age: " + age);
    }

    @FCommand(name = "hello")
    public void helloForConsole(ConsoleCommandSender sender, String name, int age) {
        event.sender().sendMessage("Name: " + name + " Age: " + age);
    }

}

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        MyHelloCommand helloCommand = new MyHelloCommand();

        Commands commands = CommandsFramework.enable(this);
        commands.create(helloCommand).register();
    }
}
```

#### Patterns

Patterns are executed as first step of building command,
Therefore we can later adjust `arguments` properties by using
`@FArgument` annotation

```java
public final class MyHelloCommand {

    @FCommand(pattern = "/spawn <coins:Number>")
    @FArgument(name = "coins", defaultValue = "20")
    public void onSpawnCommand(Player sender, double coins) {
        System.out.println("executing " + coins);
    }
}

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        MyHelloCommand helloCommand = new MyHelloCommand();

        Commands commands = CommandsFramework.enable(this);
        commands.create(helloCommand).register();
    }
}
```

#### Events

You can bind command events to the methods. All events
start with the `on` prefix. As a value you should provide
method name of the `method` you are binding to.


> `onBuilder` method is executed as a last step before registering command
>
> `onValidate` is triggered before parsing argument and checking permission
>
> `onError` is trigger whenever exception happen during command execution
>
> 'onFinalize' is trigger after the method was invoked, it can be used for post-processing

When you compile this example output will be

```
building
validating
executing
error
finalize
```

```java
public final class MyHelloCommand {

    @FCommand(pattern = "/spawn <name:Text> <coins:Number>",
            onBuild = "builder",
            onValidation = "validate",
            onError = "error",
            onFinalize="finalize")
    public void onSpawnCommand(Player sender, String name, double coins) {
        System.out.println("executing");
        var i = 0;
        i = i / 0;
        sender.sendMessage("hello world!");
    }

    private void builder(CommandBuilder builder) {
        System.out.println("building");
    }

    private boolean validate(CommandValidationEvent event) {
        System.out.println("validating");
        return true;
    }

    private void error(CommandErrorEvent event) {
        System.out.println("error");
    }
    
    private void finalize(CommandEvent event) {
        System.out.println("finalized");
    }
}

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        MyHelloCommand helloCommand = new MyHelloCommand();

        Commands commands = CommandsFramework.enable(this);
        commands.create(helloCommand).register();
    }
}
```

#### Advanced example

In this example all previously mentioned features are combined

```java
public final class MyHelloCommand {

    @FCommand(
            pattern = "/spawn <name:Text> <coins:Number>",
            description = "default description",
            usageMessage = "/spawn john 12",
            permission = "world.commands.spawn",
            hideFromCommands = false,
            shortDescription = "This is short description",
            aliases = {"Alias1", "Alias2"},
            onValidation = "validate",
            onError = "error",
            onBuild = "builder")
    @FArgument(name = "name", defaultValue = "John")
    @FArgument(name = "coins", defaultValue = "45")
    public void onSpawnCommand(Player sender, String name, double coins) {
        System.out.println("executing");
        var i = 0;
        i = i / 0;
        sender.sendMessage("hello world!");
    }

    @FCommand(name = "spawn")
    public void onSpawnCommandConsole(ConsoleCommandSender sender, String name, double coins) {
        System.out.println("executing");
        var i = 0;
        i = i / 0;
        sender.sendMessage("hello world!");
    }


    private void builder(CommandBuilder builder) {
        System.out.println("building");
    }

    private boolean validate(CommandValidationEvent event) {
        System.out.println("validating");
        return true;
    }

    private void error(CommandErrorEvent event) {
        System.out.println("error");
    }
}

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        MyHelloCommand helloCommand = new MyHelloCommand();

        Commands commands = CommandsFramework.enable(this);
        commands.create(helloCommand).register();
    }
}
```

---

### @FArgument

#### Basic example

```java

public final class MyHelloCommand {
    @FCommand(name = "hello")
    @FArgument(name = "name")
    @FArgument(name = "age", type = "Number")
    public void onTestCommand(CommandSender sender, String name, double age) {
        sender.sendMessage("Hello " + name + " " + age);
    }
}

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        MyHelloCommand helloCommand = new MyHelloCommand();

        Commands commands = CommandsFramework.enable(this);
        commands.create(helloCommand).register();
    }
}
```

#### Advanced example

```java

public final class MyHelloCommand {
    @FArgument(
            name = "name",
            type = "Text",
            displayAttributes = {DisplayAttribute.NAME, DisplayAttribute.ERROR},
            defaultValue = "john",
            description = "This argument takes name",
            allowNullOutput = false,
            onSuggestions = "suggest",
            onParse = "parse")
    @FArgument(name = "age", type = "Number")
    @FCommand(name = "hello")
    public void onTestCommand(CommandSender sender, String name, double age) {
        sender.sendMessage("Hello " + name + " " + age);
    }

    private List<String> suggest(ArgumentSuggestionEvent event) {
        return List.of("john", "adam", "mike");
    }

    private boolean parse(ArgumentParseEvent event) {

        var argument = event.nextArgument();
        if (argument.isEmpty()) {
            return false;
        }
        return true;
    }
}

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        MyHelloCommand helloCommand = new MyHelloCommand();

        Commands commands = CommandsFramework.enable(this);
        commands.create(helloCommand).register();
    }
}
```