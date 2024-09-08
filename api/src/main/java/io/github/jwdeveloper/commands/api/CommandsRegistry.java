package io.github.jwdeveloper.commands.api;

import java.util.List;

/**
 * This class is responsible to store, register, unregister
 * all the commands
 */
public interface CommandsRegistry {

    /**
     * Adds new command to registry, It will make your
     * command visible in suggestions and ready to use
     *
     * @param command the command target
     * @return false when a command with that name already exits,
     * or there was some unexpected error
     */
    boolean add(Command command);

    /**
     * Removes command from registry, and unregister it from commands
     *
     * @param command
     * @return false when there was some error while removing the command
     */
    boolean remove(Command command);

    /**
     * The list of commands. Be aware that removing
     * items from this list will not unregister command from working.
     * To do so you will need to use `CommandRegistry::remove` method
     *
     * @return the List of all registered commands
     */
    List<Command> commands();

    /**
     * Removes and unregisters all commands
     */
    void removeAll();
}
