# Patterns
Patters are text representation of the commands.
It allows to configure the whole command in one line of code


### 1. Usage

> For this example we will create command with 2 parameters
> First will be the name and second a number

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("/hello <title:Text> <points:Number>")
                .onPlayerExecute(event ->
                {
                    String title = event.getText("title");
                    double points = event.getNumber("points");
                    event.sender().sendMessage(title + ": " + points);
                })
                .register();
    }
}
```

Now we can execute created command in one of the following ways

`/hello Title 12`

`/hello Title 12.2`

`/hello 'This is very long title' 12`

`/hello "This is very long title" 12`

---

### 2. Suggestions

> For this example we will create command with 2 parameters
> First will be a player, and last a text. However, we want to have
> text with 3 options to select (Spamming,Trolling,TNT)

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("/kick <player:Player> <kick-reason:Text[Spamming,Trolling,TNT]")
                .onPlayerExecute(event ->
                {
                    Player player = event.getPlayer("player");
                    String reason = event.getText("kick-reason");
                    player.kick("Kicked: " + reason);
                })
                .register();
    }
}
```

<br>

Another way of displaying suggestion is to bind method that will return List<String>

```java
public final class ExamplePlugin extends JavaPlugin {

    //In this case method must be Static
    public static List<String> reasons(ArgumentSuggestionEvent event) {
        return List.of("Spamming", "Trolling", "TNT");
    }

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("/kick <player:Player> <kick-reason:Text[ExamplePlugin.reasons()]")
                .onPlayerExecute(event ->
                {
                    Player player = event.getPlayer("player");
                    String reason = event.getText("kick-reason");
                    player.kick("Kicked: " + reason);
                })
                .register();

        //Another way of binding suggestions, but by the property `s`
        commands.create("/kick-names  <reasons:Text(s:ExamplePlugin.reasons())>")
                .onPlayerExecute(event ->
                {
                    Player player = event.getPlayer("player");
                    String reason = event.getText("kick-reason");
                    player.kick("Kicked: " + reason);
                })
                .register();
    }
}
```

---

### 3. DisplayAttribute

Display mode helps to control how the argument is displayed.
Check out the `DisplayAttribute` enum to see all possible options.

To set DisplayAttribute in patterns you can use properties

* dn `DisplayAttribute.Name`
* dt `DisplayAttribute.Type`
* ds `DisplayAttribute.SUGGESTIONS`
* de `DisplayAttribute.ERROR`
* dd `DisplayAttribute.DESCRIPTION`
* d- `DisplayAttribute.NONE`
* da `DisplayAttribute.ALL`

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        //You can combine DisplayAttribute together
        commands.create("/kick <player:Player(dn,de)> <kick-reason:Text[Spamming,Trolling,TNT](ds)")
                .onPlayerExecute(event ->
                {
                    Player player = event.getPlayer("player");
                    String reason = event.getText("kick-reason");
                    player.kick("Kicked: " + reason);
                })
                .register();
    }
}
```

### 4. Parsing

You can bind custom method for parsing an argument

```java
public final class ExamplePlugin extends JavaPlugin {

    public Object parse(ArgumentParseEvent event)
    {
        String argument = event.nextArgument();
        if(argument.equals("1"))
            return true;
        else
            return false;
    }
    
    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        //You can combine DisplayAttribute together
        commands.create("/kick <test:Text(p=ExamplePlugin.parse())")
                .onPlayerExecute(event ->
                {
                    String test = event.getText("test");
                    event.sender().sendMessage(test);
                })
                .register();
    }
}
```

### 5. Syntax

```javascript
//ARGUMENT TYPES
//Create command `hello` with argument `title` of type `Text`
"/hello <title:Text>"

//If the argument type is not defined then by the default is Text        
"/hello <title>"

//SUGGESTIONS
//Create command `hello` with argument `title` of type `Text`
//And suggestions Red, Green, Blue
"/hello <title:Text[Red, Green, Blue]>"

//Default value
//Create command `hello` with argument `title` of type `Text`
//And suggestions Red, Green, Blue
//When player did not select any option the default value will be Red
"/hello <title:Text[Red, Green, Blue]?Red>"

//PROPERTIES
//Create command `hello` with argument `title` of type `Text`
//And property display=Name and tag=default
"/hello <title:Text(display=Name,tag=defualt)>"

//command can always has properties
"/hello(label=test) <title:Text(display=Name,tag=defualt)>"


//MIX EVERYTHING TOGETHER
"/hello(label=test) <title:Text[Red, Green, Blue](display=Name,tag=defualt)>"
```