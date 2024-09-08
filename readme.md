<div align="center" >
<a target="blank" >
<img src="https://github.com/user-attachments/assets/a3912167-835e-453e-b7f6-bb2a19b85f07" width="15%" >
</a>
</div>
<div align="center" >
<h1>Commands Framework</h1>

👾 *No more difficult commands* ️👾

<div align="center" >
<a href="https://central.sonatype.com/artifact/io.github.jwdeveloper.spigot.commands/commands-framework-core" target="blank" >
<img src="https://img.shields.io/maven-central/v/io.github.jwdeveloper.spigot.commands/commands-framework-core" width="20%" >
</a>

<a href="https://discord.gg/e2XwPNTBBr" target="blank" >
<img src="https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white" >
</a>

<a target="blank" >
<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" >
</a>
</div>
</div>

# Introduction

Have you ever face difficulty of creating even simple spigot command 
working well?
Are you a begginer and you got lost creating a command
with more then one argument?

The library offers easy and elegant approach of creating commands.

[documentation](https://jwdeveloper.github.io/CommandsFramework/#/)  
```xml
<dependency>
    <groupId>io.github.jwdeveloper.spigot.commands</groupId>
    <artifactId>commands-framework-core</artifactId>
    <version>1.0.8</version>
    <scope>compile</scope>
</dependency>
```


## **Paterns**

```xml
 /hello <name:Text> <count:Number> <job:Text[Miner, Fisherman, Farmer]>
```

```java
public final class Example extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandsApi commandsApi = CommandsFramework.enable(this);

           commands.create("/hello <name:Text> <age:number> <job:Text[Miner, Fisherman, Farmer]>")
                .onPlayerExecute(event ->
                {
                    event.sender().sendMessage("You called the hello command");

                    var name = event.getString("name");
                    var age = event.getNumber("age");
                    var job = event.getString("job");

                    event.sender().sendMessage("Name: " + name + " Age: " + age + " Job: " + job);
                }).register();
    }
}
```


## **Templates**

Is the most streight-forward way of creating command! Just 
add annotation `@FCommand` above a method and that's it!

```java
public final class MyHelloCommand
{
    @FCommand(pattern = "/hello <name:Text> <age:Number> <job:[Miner,Fisherman,Farmer]")
    public void helloCommand(CommandSender sender, String name, int age, String job)
    {
        event.sender().sendMessage("Name: " + name + " Age: " + age + " Job: " + job);
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

## Builder

Great choice for the advanced command customization.
You can have access to all command properties

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.create("hello")
                .addTextArgument("name")
                .addNumberArgument("age")
                .addArgument("job", argumentBuilder ->
                {
                    argumentBuilder.withSuggestions("Miner", "Fisherman", "Farmer");
                })
                .onPlayerExecute(event ->
                {
                    var name = event.getString("name");
                    var age = event.getNumber("age");
                    var job = event.getString("job");
                    event.sender().sendMessage("Name: " + name + " Age: " + age + " Job: " + job);
                })
                .register();
    }
}
```


# But why?

Commands are the most important element of a Spigot plugin. They are 
responsible for communication between player and code. Therefor it is 
essential to make them reliable and easy to manage.

Unfortunately the default Spigot commands registration is pretty rough. 
Most of the beginners have hard to adapt and understand it, with leads to frustration
and many unwanted bugs.

Library aims to be dead simple and extendable offers not one the 3 different approaches of creating comments.
Since that, eveyone from the begginer to the most expierenced coder will find

To see how well documented it is, check [documentation](https://jwdeveloper.github.io/CommandsFramework/#/)  

Join the support [discord](https://discord.gg/2hu6fPPeF7) and visit the `#programming` channel for questions, contributions and ideas. Feel free to make pull requests with missing/new features, fixes, etc

## Installation
1. Install the dependencie 
```xml
<dependency>
    <groupId>io.github.jwdeveloper.spigot.commands</groupId>
    <artifactId>commands-framework-core</artifactId>
    <version>1.0.8</version>
    <scope>compile</scope>
</dependency>
```
<br>

2. Create your first command
```java
public final class Example extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandsApi commandsApi = CommandsFramework.enable(this);

           commands.create("/hello <name:Text> <age:number> <job:Text[Miner, Fisherman, Farmer]>")
                .onPlayerExecute(event ->
                {
                    event.sender().sendMessage("You called the hello command");

                    var name = event.getString("name");
                    var age = event.getNumber("age");
                    var job = event.getString("job");

                    event.sender().sendMessage("Name: " + name + " Age: " + age + " Job: " + job);
                }).register();
    }
}
```

<br>

3. Call the command in game!

<div align="center" >
   <img align="center" src="https://github.com/user-attachments/assets/1248cd52-4d26-4a38-a764-df005a5d15bd"  >
</div>


## Contributing

[Library documentation for contributors](https://github.com/jwdeveloper/FluentCommands)

Your improvements are welcome! Feel free to open an <a href="https://github.com/jwdeveloper/FluentCommands/issues">issue</a> or <a href="https://github.com/jwdeveloper/FluentCommands/pulls">pull request</a>.
