package io.github.jwdeveloper.commands.api;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.CommandProperties;
import io.github.jwdeveloper.commands.api.data.events.CommandErrorEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandEventImpl;
import io.github.jwdeveloper.dependance.api.DependanceContainer;

import java.util.List;
import java.util.Optional;

/**
 * Represents a command in the Spigot plugin framework.
 * Provides methods for command execution, argument handling, and error management.
 */
public interface Command {

    /**
     * Retrieves the properties associated with this command, including name, description, and permissions.
     *
     * @return the {@link CommandProperties} of the command
     */
    CommandProperties properties();

    /**
     * Retrieves the list of arguments associated with this command.
     *
     * @return a list of {@link ArgumentProperties} defining the command's arguments
     */
    List<ArgumentProperties> arguments();

    /**
     * Retrieves the list of child commands associated with this command.
     *
     * @return a list of child {@link Command} objects
     */
    List<Command> children();

    /**
     * Checks if the command has a child command with the specified name.
     *
     * @param name the name of the child command
     * @return true if a child command with the specified name exists, false otherwise
     */
    boolean hasChild(String name);

    /**
     * Retrieves a child command by its name, if it exists.
     *
     * @param name the name of the child command
     * @return an {@link Optional} containing the child command if found, or empty if not found
     */
    Optional<Command> child(String name);

    /**
     * Executes the command with the specified sender, command label, and arguments.
     *
     * @param sender       the person who issued the command
     * @param commandLabel the label of the command being executed
     * @param arguments    the arguments passed to the command
     * @return an {@link ActionResult} containing the {@link CommandEventImpl} of the command execution
     */
    <SENDER> ActionResult<CommandEvent> executeCommand(SENDER sender, String commandLabel, String... arguments);


    /**
     * Provides suggestions for command completion based on the sender, alias, and input arguments.
     *
     * @param sender the  requesting suggestions
     * @param alias  the alias of the command being used
     * @param args   the arguments entered by the user so far
     * @return an {@link ActionResult} containing a list of suggested strings
     */
    ActionResult<List<String>> executeSuggestions(Object sender, String alias, String... args);

    /**
     * Retrieves the parent command of this command, if it exists.
     *
     * @return an {@link Optional} containing the parent command if available, or empty if no parent exists
     */
    Optional<Command> parent();

    /**
     * Checks if this command has a parent command.
     *
     * @return true if a parent command exists, false otherwise
     */
    boolean hasParent();

    /**
     * Retrieves the name of the command.
     *
     * @return the name of the command as a string
     */
    String name();

    /**
     * Retrieves the {@link DependanceContainer} associated with this command,
     * which manages dependencies and services.
     *
     * @return the associated {@link DependanceContainer}
     */
    DependanceContainer container();

    /**
     * Handles and dispatches errors that occur during command execution.
     *
     * @param errorEvent the {@link CommandErrorEvent} containing details of the error
     */
    void dispatchError(CommandErrorEvent errorEvent);

    void dispatchError(Object sender, Exception e, String... arguments);
}
