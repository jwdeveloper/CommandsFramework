package io.github.jwdeveloper.spigot.commands.impl.examples;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.spigot.commands.impl.CommandsTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubCommandsTests extends CommandsTestBase {


    @Test
    public void should_suggest_subcommand() {

        var command = createCommand();
        var suggestions = super.executeSuggestions(command.name(), "adam", "12", "");

        assertTrue(suggestions);

        var commandsNames = suggestions.getValue();

        Assertions.assertEquals(3, commandsNames.size());
        Assertions.assertEquals("sub0", commandsNames.get(0));
        Assertions.assertEquals("sub1", commandsNames.get(1));
        Assertions.assertEquals("sub2", commandsNames.get(2));
    }

    @Test
    public void should_trigger_subcommand() {

        var command = createCommand();
        var suggestions = super.execute(command.name(), "adam", "12", "sub1", "");

        assertTrue(suggestions);
        var event = suggestions.getValue();
        Assertions.assertEquals(command.child("sub1").get(), event.executedCommand().getCommand());
    }

    @Test
    public void should_trigger_subcommand_with_not_defined_parameters() {

        var command = createCommand();
        var suggestions = super.execute(command.name(), "adam", "12", "sub1", "test1", "test2");

        assertTrue(suggestions);
        var event = suggestions.getValue();
        Assertions.assertEquals(command.child("sub1").get(), event.executedCommand().getCommand());
        Assertions.assertEquals("test1", event.getString(0));
        Assertions.assertEquals("test2", event.getString(1));
    }


    private Command createCommand() {


        var builder = super.create("/test <name> <age>");


        builder.addSubCommand("sub0").onExecute(event ->
        {
            System.out.println("Hello sub0!");
        });
        builder.addSubCommand("sub1").onExecute(event ->
        {
            System.out.println("Hello sub1!");
        });
        builder.addSubCommand("sub2").onExecute(event ->
        {
            System.out.println("Hello sub2!");
        });
        return builder.register();
    }
}
