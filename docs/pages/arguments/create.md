# Creating arguments

It is pretty handy to define custom argument type 
logic in one place, and then reuse it over time.
To do so you can register your own Argument type!

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.argumentTypes()
                .create("Block")
                .onParse(event ->
                {
                    Material material = Material.getMaterial(event.iterator().next());
                    if (!material.isBlock()) {
                        throw new RuntimeException("Provided value is not block!");
                    }
                    return material;
                })
                .onSuggestion(event ->
                {
                    String input = event.rawValue();
                    return Arrays.stream(Material.values())
                            .filter(Material::isBlock)
                            .filter(e -> e.name().contains(input))
                            .limit(10)
                            .map(Enum::name)
                            .toList();
                })
                .register();

        commands.create("hello <test:Block>")
                .onExecute(event ->
                {
                    Material block = event.getArgument("test", Material.class);
                    event.sender().sendMessage(block.name()+" selected block!");
                })
                .register();
    }
}
```