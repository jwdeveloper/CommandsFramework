```java
public final class MyHelloCommand
{
    @FCommand(pattern = "/hello <name:Text?john> <age:Number> <job:[Miner,Fisherman,Farmer]")
    public void helloCommand(CommandSender sender, String name, int age, String job)
    {
        sender.sendMessage("Name: " + name + " Age: " + age + " Job: " + job);
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
