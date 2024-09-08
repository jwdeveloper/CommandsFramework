package io.github.jwdeveloper.commands.api.data.events;

import io.github.jwdeveloper.commands.api.data.expressions.ArgumentNode;
import io.github.jwdeveloper.commands.api.data.expressions.CommandNode;
import lombok.Setter;

import java.util.List;

/**
 * Event is executed when command has been successfully invoked
 *
 * @param <SENDER> the sender type of object that trigger the command
 */
public interface CommandEvent<SENDER> {


    /**
     * Sender is an actor that caused the command execution
     *
     * @return the instance of sender
     */
    SENDER sender();

    /**
     * The last executed command of the commands chain
     *
     * @return the last command
     */
    CommandNode executedCommand();

    /**
     * The command chain of all the executed commands
     *
     * @return the command chain
     */
    List<CommandNode> commands();


    Object output();

    Object output(Object value);

    List<ArgumentNode> arguments();

    int argumentCount();

    ArgumentNode argument(int index);

    <T> T getArgument(String name, Class<? extends T> type);

    boolean hasArgument(int argumentIndex);

    boolean hasArgument(String argumentName);

    String getString(int index);

    String getString(String argumentName);

    Double getNumber(int index);

    Boolean getBool(String argumentName);

    Boolean getBool(int index);
}
