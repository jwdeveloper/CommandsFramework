package io.github.jwdeveloper.commands.core.impl.services;

import io.github.jwdeveloper.dependance.injector.api.util.Pair;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.expressions.CommandExpression;


import java.util.*;

public class ExpressionService {

    private final DefaultValidationService validationService;
    private final CommandParser parser;

    public ExpressionService(DefaultValidationService validationService, CommandParser commandParser) {
        this.validationService = validationService;
        this.parser = commandParser;
    }

    public ActionResult<CommandExpression> parse(
            Command command,
            Object sender,
            boolean forSuggestions,
            String... args) {
        var expression = new CommandExpression();
        expression.setRawValue(args);
        expression.setCommandNodes(new ArrayList<>());

        for (var pair : getCommandsAndParams(command, args)) {
            command = pair.getKey();
            args = pair.getValue();

            var validationCheck = validationService.validateCommand(command, sender, args);
            if (validationCheck.isFailed()) {
                return validationCheck.cast();
            }

            var result = parser.parseCommand(command, sender, forSuggestions, args);
            if (result.isFailed()) {
                return (ActionResult) result;
            }

            var parsedValue = result.getValue();
            expression.getCommandNodes().add(parsedValue);
        }
        return ActionResult.success(expression);
    }

    public List<Pair<Command, String[]>> getCommandsAndParams(Command command, String[] args) {
        var result = new ArrayList<Pair<Command, String[]>>();
        var currentCommand = command;
        int startIndex = 0;
        for (int i = 0; i < args.length; i++) {
            var currentArg = args[i];
            if (currentCommand.hasChild(currentArg)) {
                var commandArgs = Arrays.copyOfRange(args, startIndex, i);
                result.add(new Pair<>(currentCommand, commandArgs));
                currentCommand = currentCommand.child(currentArg).get();
                startIndex = i + 1;
            }
        }
        var commandArgs = Arrays.copyOfRange(args, startIndex, args.length);
        result.add(new Pair<>(currentCommand, commandArgs));
        return result;
    }
}
