package io.github.jwdeveloper.commands.core.impl.services;

import io.github.jwdeveloper.commands.api.Command;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;
import io.github.jwdeveloper.commands.api.data.expressions.ArgumentNode;
import io.github.jwdeveloper.commands.api.data.expressions.CommandNode;
import io.github.jwdeveloper.commands.api.iterators.ArgumentIterator;

import java.util.ArrayList;

public class CommandParser {

    public ActionResult<CommandNode> parseCommand(Command command, Object sender, boolean forSuggestions, String... args) {
        var argumentsOutput = new ArrayList<ArgumentNode>();
        var iterator = new ArgumentIterator(args);
        var event = new ArgumentParseEvent();
        event.command(command);
        event.iterator(iterator);
        event.sender(sender);

        var invalid = false;
        for (var argument : command.arguments()) {
            if (forSuggestions) {
                if (iterator.isLastToken()) {
                    break;
                }
                //When current token is space better arguments, skip it
                if (iterator.isLookup("")) {
                    iterator.next();
                }

                //When current token is last and is space then return argument as valid
                if (iterator.isLastToken() && iterator.isCurrent("")) {
                    argumentsOutput.add(new ArgumentNode(argument, iterator.current(), true, true, ""));
                    break;
                }
            }
            event.argument(argument);
            var result = handleSingleArgument(event, iterator, argument);
            if (result.isSuccess()) {
                argumentsOutput.add(result.getValue());
                continue;
            }
            //When validation is invalid break loop and return argument as invalid
            argumentsOutput.add(new ArgumentNode(argument, iterator.current(), false, false, result.getMessage()));
            invalid = true;
            break;
        }


        if (iterator.hasNext() && !invalid) {
            argumentsOutput.add(new ArgumentNode(null, iterator.next(), true, true, ""));
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

        if (!iterator.hasNext()) {
            if (argument.required()) {
                return ActionResult.failed("This argument is empty!");
            }
            iterator.append(argument.defaultValue());
        }

        if (iterator.isLookup("~")) {
            iterator.replace(1, argument.defaultValue());
        }

        Object output = null;
        var parser = argument.parser();
        if (parser != null) {
            var parseResult = parser.onParse(event);
            if (parseResult.isFailed())
            {
                return ActionResult.failed(parseResult.getMessage());
            }
            output = parseResult.getValue();
        }

        if (output == null && !argument.allowNullOutput()) {
            return ActionResult.failed("Argument " + argument.name() + " parsed value is Null!");
        }

        var result = new ArgumentNode(argument, output, false, true, "");
        return ActionResult.success(result);
    }


}
