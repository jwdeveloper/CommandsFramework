package io.github.jwdeveloper.commands.api.data.events;

import io.github.jwdeveloper.commands.api.Command;

import io.github.jwdeveloper.commands.api.data.expressions.ArgumentNode;
import io.github.jwdeveloper.commands.api.data.expressions.CommandNode;
import io.github.jwdeveloper.commands.api.exceptions.ArgumentException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;


@Getter
@Accessors(fluent = true)
public class CommandEventImpl<T> implements CommandEvent<T> {
    private final T sender;
    private final CommandNode executedCommand;
    private final List<CommandNode> commands;

    @Setter
    private Object output;

    public CommandEventImpl(T sender, List<CommandNode> invokedCommands) {
        this.sender = sender;
        this.commands = invokedCommands;
        this.executedCommand = invokedCommands.get(invokedCommands.size() - 1);
    }

    @Override
    public CommandNode executedCommand() {
        return executedCommand;
    }

    public List<ArgumentNode> arguments() {
        return executedCommand.getArguments();
    }

    public int argumentCount() {
        return executedCommand.getArguments().size();
    }

    public ArgumentNode argument(int index) {
        return executedCommand.getArguments().get(index);
    }


    public boolean hasArgument(int argumentIndex) {
        return argumentCount() > argumentIndex;
    }

    public boolean hasArgument(String argumentName) {

        for (var i = commands.size() - 1; i > 0; i--) {
            var cmd = commands.get(i);
            var arg = cmd.getArguments()
                    .stream()
                    .filter(e -> e.getArgument().name().equalsIgnoreCase(argumentName))
                    .findFirst();

            if (arg.isPresent()) {
                return true;
            }
        }
        return false;
    }

    public <T> T getArgument(int argumentIndex, Class<? extends T> type) {
        if (argumentIndex > argumentCount() - 1) {
            if (argumentIndex <= executedCommand.getRaw().length - 1) {
                if (!String.class.equals(type)) {
                    throw new ArgumentException("Argument for index " + argumentIndex + " not exists, must be type of String");
                }
                return (T) executedCommand.getRaw()[argumentIndex];
            }
            throw new ArgumentException("Argument for index " + argumentIndex + " not exists!");
        }

        var value = executedCommand.getArgument(argumentIndex).getValue();
        if (!type.isAssignableFrom(value.getClass())) {
            throw new ArgumentException("Type mishmash: Argument has type: " + value.getClass().getSimpleName() + " but you are trying to get " + type.getSimpleName());
        }
        return (T) value;
    }

    public <T1> T1 getArgument(String name, Class<? extends T1> type) {
        ArgumentNode argument = null;
        for (var i = commands.size() - 1; i >= 0; i--) {
            var cmd = commands.get(i);
            var optional = cmd.getArguments()
                    .stream()
                    .filter(e -> e.getArgument().name().equalsIgnoreCase(name))
                    .findFirst();

            if (optional.isEmpty()) {
                continue;
            }
            argument = optional.get();
            break;
        }

        if (argument == null) {
            throw new ArgumentException("Argument with the name " + name + " not exists!");
        }
        var value = argument.getValue();
        if (!type.isAssignableFrom(value.getClass())) {
            throw new ArgumentException("Type mishmash: Argument has type: " + value.getClass().getSimpleName() + " but you are trying to get " + type.getSimpleName());
        }

        return (T1) value;
    }

    public String getString(String argumentName) {
        return getArgument(argumentName, String.class);
    }

    public String getText(String argumentName) {
        return getString(argumentName);
    }

    public String getString(int argument) {
        return getArgument(argument, String.class);
    }

    public Double getNumber(String argument) {
        return getArgument(argument, Double.class);
    }

    public Double getNumber(int index) {
        return getArgument(index, Double.class);
    }

    public Command command() {
        return executedCommand.getCommand();
    }

    public Boolean getBool(String argument) {
        return getArgument(argument, Boolean.class);
    }
}
