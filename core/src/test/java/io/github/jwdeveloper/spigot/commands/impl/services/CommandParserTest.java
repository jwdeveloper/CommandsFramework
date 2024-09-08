package io.github.jwdeveloper.spigot.commands.impl.services;

import io.github.jwdeveloper.commands.core.impl.services.CommandParser;
import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.spigot.commands.impl.common.CommandsTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandParserTest extends CommandsTestBase {
    CommandParser parser;

    @Override
    protected void onBefore(Commands commands) {
        parser = new CommandParser();
    }


    @Test
    public void should_add_closing_argument() {
        var command = create("/test <age:Number> <name:Text>").register();
        var result = parser.parseCommand(command, sender, false, "123", "\"", "john", "\"", "something");

        assertTrue(result);
        var value = result.getValue();
        assertEquals(3, value.getArguments().size());

        var arg1 = value.getArgument(0);
        assertEquals(123.0d, arg1.getValue());

        var arg2 = value.getArgument(1);
        assertEquals("john", arg2.getValue());

        var arg3 = value.getArgument(2);
        Assertions.assertTrue(arg3.isEnd());


        Assertions.assertTrue(value.getLastResolvedArgument().isSuccess());
    }

    @Test
    public void should_has_all_values_parsed() {
        var command = create("/test <age:Number> <name:Text>").register();
        var result = parser.parseCommand(command, sender, false, "123", "\"", "john", "is", "my", "brother", "\"");

        assertTrue(result);
        var value = result.getValue();
        assertEquals(2, value.getArguments().size());

        var arg1 = value.getArgument(0);
        assertEquals(123.0d, arg1.getValue());

        var arg2 = value.getArgument(1);
        assertEquals("john is my brother", arg2.getValue());

        Assertions.assertTrue(value.getLastResolvedArgument().isSuccess());
    }

    @Test
    public void should_has_first_value_parsed() {
        var command = create("/test <age:Number> <name:Text>").register();
        var result = parser.parseCommand(command, sender, false, "123", "Hello");


        assertTrue(result);
        var value = result.getValue();
        assertEquals(2, value.getArguments().size());

        var arg1 = value.getArgument(0);
        assertEquals(123.0d, arg1.getValue());

        var arg2 = value.getArgument(1);
        assertEquals("Hello", arg2.getValue());
    }

    @Test
    public void should_get_params_default_values() {
        var command = create("/test <age:Number?12> <name:Text?John>").register();
        var result = parser.parseCommand(command, sender, false, "~", "~");


        assertTrue(result);
        var value = result.getValue();
        assertEquals(2, value.getArguments().size());

        var arg1 = value.getArgument(0);
        assertEquals(12.0, arg1.getValue());

        var arg2 = value.getArgument(1);
        assertEquals("John", arg2.getValue());
    }

    @Test
    public void should_throw_when_default_value_is_not_profiled_and_argument_is_required() {
        var command = create("/test <age:Number?12> <name:Text>").register();
        var result = parser.parseCommand(command, sender, false);
        assertTrue(result);

        var value = result.getValue();
        assertEquals(true, value.getArgument(0).isValid());
        assertEquals(false, value.getArgument(1).isValid());

        assertEquals("This argument is empty!", value.getArgument(1).getValidationMessage());
    }
}
