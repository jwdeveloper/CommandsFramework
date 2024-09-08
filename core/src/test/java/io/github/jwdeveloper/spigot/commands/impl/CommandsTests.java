package io.github.jwdeveloper.spigot.commands.impl;

import io.github.jwdeveloper.spigot.commands.impl.common.CommandsTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandsTests extends CommandsTestBase {


    @Test
    public void shoud_return_name_of_first_child() {
        create("/test <name:Text> <age:Number> <team:Text>")
                .addSubCommand("sub1", commandBuilder ->
                {

                })
                .register();
        var commandResult = executeSuggestions("test", "john", "12.2", "Farmer", "");

        Assertions.assertTrue(commandResult.isSuccess());

        var value = commandResult.getValue();
        Assertions.assertEquals(1, value.size());
        Assertions.assertEquals("sub1", value.get(0));
    }

    @Test
    public void should_return_name_of_second_child() {
        var builder = create("/test <name:Text> <age:Number> <team:Text>");
        builder.addSubCommand("sub1").addSubCommand("sub2");
        builder.register();


        var commandResult = executeSuggestions("test", "john", "12.2", "Farmer", "sub1", "");

        Assertions.assertTrue(commandResult.isSuccess());

        var value = commandResult.getValue();
        Assertions.assertEquals(1, value.size());
        Assertions.assertEquals("sub2", value.get(0));
    }


    @Test
    public void should_parse_command() {
        create("/test <age:Number> <name:Text>").register();
        var commandResult = execute("test", "1", "'", "this", "is", "bad", "idea", "'");

        Assertions.assertTrue(commandResult.isSuccess());

        var value = commandResult.getValue();
        Assertions.assertEquals(1, value.getNumber(0));
        Assertions.assertEquals("this is bad idea", value.getString(1));
    }

    @Test
    public void should_not_parse_command() {
        create("/test <age:Number> <name:Text>").register();
        var commandResult = execute("test", "1");

        assertFalse(commandResult);

        var value = commandResult.getValue();
        Assertions.assertEquals(true, value.argument(0).isValid());
        Assertions.assertEquals(false, value.argument(1).isValid());
    }


    @Test
    public void should_parse_command_when_argument_has_unwanted_spaces() {
        create("/test <label:Text[One,Two, Three](ds)> <name:Text>").register();
        var commandResult = executeSuggestions("test", "example ", " test ");

        assertTrue(commandResult);

        var value = commandResult.getValue();
        Assertions.assertEquals("<name:Text>", value.get(0));
    }


    @Test
    public void shouldParseWithError() {
        create("/test <age:Number> <name:Text>").register();
        var commandResult = executeSuggestions("test", "1.ad");
        assertTrue(commandResult);
        var value = commandResult.getValue();
        Assertions.assertEquals(1, value.size());
        Assertions.assertEquals("<age:Number ! It's number, not text!>", value.get(0));
    }


    @Test
    public void should_error_when_no_color_closed() {
        create("/test <age:Number> <name:Text>").register();
        var commandResult = executeSuggestions("test", "1", "'", "my", "name");

        assertTrue(commandResult);

        var value = commandResult.getValue();
        Assertions.assertEquals(1, value.size());
        Assertions.assertEquals("<name:Text ! Unmatched quotation marks.>", value.get(0));
    }

}
