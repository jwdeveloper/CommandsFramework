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
