# Command builder

<br>

Builder is the most basic way of create commands.
It provides access for a command properties and features.

> The name `builder` comes from the design pattern [Builder](https://refactoring.guru/design-patterns/builder)
> that the code is build upon.

Builder features

- [Properties](#properties) - Configure command property like `name` `description` `permissions`
- [Arguments](#arguments) - Adding and configuring command's arguments
- [Events](#events) - Listen for command's events `onPlayerExecute`, `onValidation`, `onError`
- [Children](#children) - Subcommands

---

### Properties

> All the properties are `optional`. For this example they are set just
> to show all possibilities

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("hello")
                //command label
                .withLabel("Hello")
                //aliases
                .withAliases("Hello-Alias", "Hello-World")
                //permission
                .withPermission("plugin.command.hello")
                //Can not be executed by the console and proxy
                .withExcludedSenders(SenderType.CONSOLE, SenderType.PROXY)
                //Command will not be visible in suggestions
                .withHideFromSuggestions(false)
                //When false command will not be able to execute
                .withIsActive(true)
                //You can attach custom data to command
                .withProperty("custom-tag", 123)
                .withShortDescription("This is the hello command")
                .withUsageMessage("type /hello to use this command")
                .withDescription("This is the longer description of command")
                .register();
    }
}
```

### Arguments

> Arguments also has own properties and again all the properties are `optional`.

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("hello")
                .addArgument("item", argumentBuilder ->
                {
                    argumentBuilder.withType("Text"); //Argument Type
                    argumentBuilder.withIndex(1); //Define in what order argument will be display
                    argumentBuilder.withSuggestions("RED", "GREEN", "BLUE", "CAT", "DOG");
                    argumentBuilder.withDefaultValue("RED"); //Default value for argument
                    argumentBuilder.withDescription("This is the item argument");

                    //With the DisplayAttribute you can control how the argument is display.
                    //for this example we only want to display Name and Type
                    //so the argument will be previewed as <item:Text>
                    argumentBuilder.withDisplayAttribute(DisplayAttribute.NAME, DisplayAttribute.TYPE);
                })
                .addNumberArgument("amount", argumentBuilder ->
                {
                    argumentBuilder.withIndex(0); //The argument will be display as first since index is 0
                })
                .register();
    }
}
```

> To simplify adding arguments there is a bunch of methods for adding
> build-in argument types like `TextArgument`, `NumberArgument`, `BoolArgument` etc..

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("hello")
                .addNumberArgument("age") //Number argument will parse input into Double 
                .addTextArgument("last-name")//Text argument can take as input value between semicolons /hello "This is argument"
                .addBoolArgument("op")//Bool takes True/False
                .addPlayerArgument("selected-player")//Player argument returns list of the all online players
                .addColorArgument()//Minecraft color
                .addSoundArgument()//Minecraft sounds
                .addParticleArgument()//Minecraft particles
                .addLocationArgument()//The location is given format <x:Number> <y:Number> <z:Number>
                .addEntityArgument()//Minecraft Entity types
                .register();
    }
}
```

### Events

> Builder has build-in events system that allows easily listen 
> for certain action triggered while command is invoked

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("hello")
                .onExecute(event ->
                {
                    event.sender().sendMessage("Executed whoever trigger command");
                })
                .onPlayerExecute(event -> event.sender().sendMessage("Executed when player"))
                .onConsoleExecute(event -> event.sender().sendMessage("Executed when console"))
                .onProxyExecute(event -> event.sender().sendMessage("Executed when proxy"))
                .onRemoteConsoleExecute(event -> event.sender().sendMessage("Executed when remote console"))
                .onBlockExecute(event -> event.sender().sendMessage("Executed when remote block"))
                .onValidation(event ->
                {
                    //Triggered before argument got parsed you can manually validate the command
                    var sender = event.getSender();
                    if (sender.getName().equals("Mark")) {
                        return ActionResult.failed("Make you are not welcome!");
                    }
                    return ActionResult.success();
                })
                .onError(event ->
                {
                    //Triggered when exception was thrown during command execution
                    event.getException().printStackTrace();
                })
                .register();
    }
}
```

### Children 
> Commands can have children(subCommands)
> the first parameter is children name and second is lambda with children command builder

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("hello")
                .addSubCommand("child1",builder ->
                {
                    //child1 will be not visible in suggestions
                    builder.withHideFromSuggestions(false); 
                    builder.addTextArgument("name");
                })
                .addSubCommand("child2",builder ->
                {
                    builder.addNumberArgument("points");
                })
                .register();
    }
}
```

