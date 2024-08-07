package io.github.jwdeveloper.spigot.commands.services;

import io.github.jwdeveloper.spigot.commands.Command;
import io.github.jwdeveloper.spigot.commands.argumetns.ArgumentProperties;
import io.github.jwdeveloper.spigot.commands.data.ActionResult;
import io.github.jwdeveloper.spigot.commands.data.events.ArgumentParseEvent;
import io.github.jwdeveloper.spigot.commands.data.expressions.ArgumentNode;
import io.github.jwdeveloper.spigot.commands.data.expressions.CommandNode;
import io.github.jwdeveloper.spigot.commands.iterators.ArgumentIterator;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CommandParser {

    public ActionResult<CommandNode> parseCommand(Command command, CommandSender sender, String... args) {
        var argumentsOutput = new ArrayList<ArgumentNode>();
        var iterator = new ArgumentIterator(args);
        var event = new ArgumentParseEvent();
        event.command(command);
        event.iterator(iterator);
        event.sender(sender);
        for (var argument : command.arguments()) {
            event.argument(argument);
            var result = handleSingleArgument(event, iterator, argument);
            if (result.isFailed()) {
                return (ActionResult) result;
            }
            argumentsOutput.add(result.getValue());
        }

        if (iterator.hasNext()) {
            argumentsOutput.add(new ArgumentNode(null, iterator.next(), true, true));
        }

        var result = new CommandNode();
        result.setRaw(args);
        result.setCommand(command);
        result.setArguments(argumentsOutput);
        return ActionResult.success(result);
    }

    private ActionResult<ArgumentNode> handleSingleArgument(ArgumentParseEvent event,
                                                            ArgumentIterator iterator,
                                                            ArgumentProperties argument) {
        var defaultValueProvided = false;
        if (!iterator.hasNext()) {
            if (argument.required()) {
                var action = (ActionResult) ActionResult.failed(argument, "This argument is required but value is not provided!");
                return action;
            }
            iterator.append(argument.defaultValue());
            defaultValueProvided = true;
        }

        Object output = null;
        var parser = argument.parser();
        if (parser != null) {
            var parseResult = parser.onParse(event);
            if (parseResult.isFailed()) {
                return (ActionResult) ActionResult.failed(argument, parseResult.getMessage());
            }
            output = parseResult.getValue();
        }

        if (output == null) {
            return ActionResult.failed("Argument " + argument.name() + " parsing value is Null!");
        }

        var result = new ArgumentNode(argument, output, defaultValueProvided, false);
        return ActionResult.success(result);
    }


}
