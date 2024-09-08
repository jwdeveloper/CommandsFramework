package io.github.jwdeveloper.commands.spigot.tests.examples;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.commands.api.data.events.CommandEventImpl;
import io.github.jwdeveloper.commands.core.impl.services.ExpressionService;
import io.github.jwdeveloper.commands.spigot.tests.common.SpigotTestBase;
import io.github.jwdeveloper.commands.api.exceptions.ArgumentException;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandEventTest extends SpigotTestBase {


    Command command;
    ExpressionService service;

    @Override
    protected void onBefore(Commands commands) {
        command = create("/hello <name:Text> <age:Number> <job:Text>").register();
        service = command.container().find(ExpressionService.class);
    }

    @Test
    public void should_have_arguments_values() {


        var expresion = service.parse(command, playerSender, false, "john", "12", "farmer");

        assertTrue(expresion);

        var event = new CommandEventImpl<Player>(playerSender, expresion.getValue().getCommandNodes());


        Assertions.assertTrue(event.hasArgument(0));
        Assertions.assertTrue(event.hasArgument(1));
        Assertions.assertTrue(event.hasArgument(2));


        Assertions.assertEquals(3, event.argumentCount());
        Assertions.assertEquals("john", event.getString(0));
        Assertions.assertEquals("john", event.getString("name"));

        Assertions.assertEquals(12.0d, event.getNumber(1));
        Assertions.assertEquals(12.0d, event.getNumber("age"));

        Assertions.assertEquals("farmer", event.getString(2));
        Assertions.assertEquals("farmer", event.getString("job"));
    }


    @Test
    public void should_throw_when_not_existst() {

        var expresion = service.parse(command, playerSender, false, "john", "12", "farmer");
        assertTrue(expresion);
        var event = new CommandEventImpl<Player>(playerSender, expresion.getValue().getCommandNodes());

        Assertions.assertFalse(event.hasArgument(4));
        Assertions.assertFalse(event.hasArgument("not exists"));
        Assertions.assertThrows(ArgumentException.class, () ->
        {
            event.getString(4);
        });
        Assertions.assertThrows(ArgumentException.class, () ->
        {
            event.getString("not exists");
        });
    }

    @Test
    public void should_throw_when_type_mishmash() {

        var expresion = service.parse(command, playerSender, false, "john", "12", "farmer");
        assertTrue(expresion);
        var event = new CommandEventImpl<Player>(playerSender, expresion.getValue().getCommandNodes());


        Assertions.assertThrows(ArgumentException.class, () ->
        {
            event.getNumber(0);
        });
        Assertions.assertThrows(ArgumentException.class, () ->
        {
            event.getArgument("not existing", Boolean.TYPE);
        });
        Assertions.assertThrows(ArgumentException.class, () ->
        {
            event.getNumber(2);
        });
    }
}
