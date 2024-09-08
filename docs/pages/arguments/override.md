# Overriding arguments 

When you feel that default implementation 
an argument does not meet your needs. You can
override an argument type with your implementation

```java
public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Commands commands = CommandsFramework.enable(this);
        commands.argumentTypes()
                .create("Text")
                .onParse(event ->
                {
                    var argument = event.currentArgument();
                    if (argument.isEmpty()) {
                        throw new RuntimeException("Value is empty!");
                    }
                    return argument + "_parsed!";
                })
                .onSuggestion(argumentSuggestionEvent ->
                {
                    return List.of("Hello", "World");
                })
                .register();

        commands.create("hello <test:Text>")
                .onExecute(event ->
                {
                    var parsedValue = event.getText("test");
                    event.sender().sendMessage(parsedValue);
                })
                .register();
    }
}
```