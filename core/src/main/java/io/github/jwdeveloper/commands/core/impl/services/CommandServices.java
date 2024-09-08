package io.github.jwdeveloper.commands.core.impl.services;


import io.github.jwdeveloper.commands.api.data.events.CommandEvent;
import io.github.jwdeveloper.dependance.api.DependanceContainer;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import io.github.jwdeveloper.commands.api.data.DisplayAttribute;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandEventImpl;
import io.github.jwdeveloper.commands.api.services.MessagesService;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;


@Accessors(fluent = true)
@Getter
public class CommandServices {

    private final CommandsRegistry registry;
    private final ExpressionService expressions;
    private final DependanceContainer container;
    private final MessagesService messages;

    public CommandServices(
            CommandsRegistry registry,
            DependanceContainer container,
            ExpressionService expressionService,
            MessagesService messagesService) {
        this.registry = registry;
        this.container = container;
        this.messages = messagesService;
        this.expressions = expressionService;
    }

    public ActionResult<CommandEvent> execute(Command command,
                                              Object sender,
                                              String commandLabel,
                                              String[] commandArguments) {
        var expressionResult = expressions.parse(command, sender, false, commandArguments);
        if (expressionResult.isFailed()) {
            return expressionResult.cast();
        }

        var expression = expressionResult.getValue();
        var invokedCommandNode = expression.invokedCommand();
        var invokedCommand = invokedCommandNode.getCommand();

        var event = new CommandEventImpl(sender, expression.getCommandNodes());
        var eventsService = invokedCommand.container().find(CommandEventsImpl.class);

        return eventsService.publishCommandInvoked(event);
    }

    public List<String> executeTab(Command command,
                                   Object sender,
                                   String alias,
                                   String[] args) {

        var expressionResult = expressions.parse(command, sender, true, args);
        if (expressionResult.isFailed()) {
            return List.of(expressionResult.getMessage());
        }
        var expression = expressionResult.getValue();
        var invokedCommand = expression.invokedCommand();
        if (invokedCommand.getArguments().isEmpty()) {
            return Collections.emptyList();
        }

        var argumentNode = invokedCommand.lastArgument();
        var argument = argumentNode.getArgument();

        if (!argumentNode.isValid()) {
            var displayResult = messages.displayArgument(argument, argumentNode.getValidationMessage());
            if (displayResult.isSuccess()) {
                return List.of(displayResult.getValue());
            }
        }

        if (argumentNode.isEnd() && argument == null) {
            return invokedCommand.getCommand()
                    .children()
                    .stream()
                    .map(Command::name)
                    .toList();
        }

        if (!argument.hasDisplayAttribute(DisplayAttribute.SUGGESTIONS)) {
            var displayResult = messages.displayArgument(argument, argumentNode.getValidationMessage());
            if (displayResult.isSuccess()) {
                return List.of(displayResult.getValue());
            }
        }

        var argumentEvent = new ArgumentSuggestionEvent();
        argumentEvent.argument(argument);
        argumentEvent.command(command);
        argumentEvent.sender(sender);
        argumentEvent.value(argumentNode.getValue());
        argumentEvent.rawValue(argumentNode.getValue() + "");


        var result = argument.suggestion().onSuggestion(argumentEvent);
        if (result.isFailed()) {
            throw new RuntimeException(result.getMessage());
        }
        var values = result.getValue();
        var argumentValue = argumentNode.getValue().toString();
        if (!argumentValue.isEmpty() && !values.contains(argumentValue)) {

//            var copy = new ArrayList<String>();
//            copy.add("[!" + argumentValue + " is not acceptable !]");
//            copy.addAll(values);
//            return copy;
        }
        return values;
    }


}
