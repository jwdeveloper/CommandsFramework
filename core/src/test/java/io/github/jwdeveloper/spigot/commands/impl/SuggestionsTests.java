package io.github.jwdeveloper.spigot.commands.impl;

import io.github.jwdeveloper.commands.api.data.DisplayAttribute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SuggestionsTests extends CommandsTestBase {


    @Test
    public void should_suggest_subcommand() {

        var command = create("/test");
        command.addArgument("arg1");

        command.addSubCommand("sub0");
        command.addSubCommand("sub1");
        command.addSubCommand("sub2");
        command.register();

        var suggestions = super.executeSuggestions("test", "arg1Input", "");
        assertTrue(suggestions);
        var commandsNames = suggestions.getValue();
        Assertions.assertEquals(3, commandsNames.size());
        Assertions.assertEquals("sub0", commandsNames.get(0));
        Assertions.assertEquals("sub1", commandsNames.get(1));
        Assertions.assertEquals("sub2", commandsNames.get(2));
    }


    @Test
    public void should_show_suggestions() {
        var suggestions = List.of("one", "two", "three");
        var command = create("/test <arg1>");
        command.argument("arg1")
                .withDisplayAttribute(DisplayAttribute.SUGGESTIONS)
                .withSuggestions(suggestions);
        command.register();

        var result = executeSuggestions("test", "");

        assertTrue(result);
        Assertions.assertEquals("one", result.getValue().get(0));
        Assertions.assertEquals("two", result.getValue().get(1));
        Assertions.assertEquals("three", result.getValue().get(2));
    }


    @Test
    public void should_not_show_suggestions() {
        var suggestions = List.of("one", "two", "three");
        var command = create("/test <arg1>");
        command.argument("arg1")
                .withSuggestions(suggestions)
                .withDisplayAttribute(DisplayAttribute.NONE);
        command.register();

        var result = executeSuggestions("test", "");

        assertTrue(result);
        Assertions.assertEquals(0, result.getValue().size());
    }

    @Test
    public void should_suggest_name_and_type() {
        var command = create("/test <arg1>");
        command.argument("arg1");
        command.register();

        var result = executeSuggestions("test", "");

        assertTrue(result);
        Assertions.assertEquals(1, result.getValue().size());
        Assertions.assertEquals("<arg1:Text>", result.getValue().get(0));
    }

    @Test
    public void should_suggestion_for_the_second_argument() {
        var suggestions = List.of("one", "two", "three");
        var command = create("/test <arg1> <arg2>");

        command.argument("arg2")
                .withDisplayAttribute(DisplayAttribute.SUGGESTIONS)
                .withSuggestions(suggestions);
        command.register();
        var result = executeSuggestions("test", "John", "");

        assertTrue(result);
        Assertions.assertEquals("one", result.getValue().get(0));
        Assertions.assertEquals("two", result.getValue().get(1));
        Assertions.assertEquals("three", result.getValue().get(2));
    }

    @Test
    public void should_show_suggestions_for_the_first_argument() {
        var suggestions = List.of("one", "two", "three");
        var command = create("/test <arg1> <arg2>");

        command.argument("arg2")
                .withDisplayAttribute(DisplayAttribute.SUGGESTIONS)
                .withSuggestions(suggestions);
        command.register();
        var result = executeSuggestions("test", "Joh");

        assertTrue(result);
        Assertions.assertEquals(1, result.getValue().size());
        Assertions.assertNotEquals("one", result.getValue().get(0));
    }
}
