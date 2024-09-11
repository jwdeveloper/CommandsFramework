package io.github.jwdeveloper.commands.api;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentsTypesRegistry;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.builders.DefaultCommandBuilder;
import io.github.jwdeveloper.commands.api.data.CommandProperties;
import io.github.jwdeveloper.commands.api.patterns.PatternsRegistry;
import io.github.jwdeveloper.commands.api.services.ActionsRegistry;
import io.github.jwdeveloper.commands.api.services.CommandSenderRegistry;
import io.github.jwdeveloper.dependance.api.DependanceContainer;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;


/**
 * Interface for managing commands
 */
public interface Commands {

    DependanceContainer container();

    PatternsRegistry patterns();

    /**
     * Argument types registry object, modify of create new commands
     * arguments
     *
     * @return a new instance of {@link ArgumentsTypesRegistry}.
     */
    ArgumentsTypesRegistry argumentTypes();

    /**
     * Command sender registry object, modify to add new valid command senders
     * @return an instance of {@link CommandSenderRegistry}.
     */
    CommandSenderRegistry commandSenders();

    /**
     * Creates a new command builder for a command with the specified pattern.
     *
     * @param pattern the pattern for the command.
     * @return a new instance of {@link CommandBuilder}.
     */
    CommandBuilder create(String pattern);

    /**
     * Creates a new command builder for a command using the specified template object.
     * Also register templateObject into the actions registry
     *
     * @param templateObject the template object for the command.
     * @return a new instance of {@link CommandBuilder}.
     */
    CommandBuilder create(Object templateObject);

    /**
     * Adds the specified command to the command manager.
     *
     * @param command the command to be added.
     */
    void register(Command command);

    /**
     * Removes the specified command from the command manager.
     *
     * @param command the command to be removed.
     */
    void unregister(Command command);

    /**
     * Removes all commands from the command manager.
     */
    void removeAll();

    /**
     * Finds and returns all commands managed by the command manager.
     *
     * @return a list of all commands.
     */
    List<Command> findAll();

    /**
     * Finds and returns commands that match the specified predicate.
     *
     * @param predicate the predicate to filter commands.
     * @return a stream of commands that match the predicate.
     */
    Stream<Command> findBy(Predicate<Command> predicate);

    /**
     * Finds and returns a command with the specified name.
     *
     * @param commandName the name of the command.
     * @return an optional containing the command if found, or empty if not found.
     */
    default Optional<Command> findByName(String commandName) {
        return findBy(command -> command.properties().name().equals(commandName)).findFirst();
    }

    /**
     * Allows to register a specific actions that can be later
     * referred to in the patterns or annotations
     *
     * @return the instance of the actions registry
     */
    ActionsRegistry actions();

}