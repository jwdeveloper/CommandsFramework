package io.github.jwdeveloper.commands.api.services;

import io.github.jwdeveloper.commands.api.data.CommandProperties;

import java.util.Set;

/* Created by Conor on 10.09.2024 */
public interface CommandSenderRegistry {

    /**
     * Registers the given class for future use as a command sender
     * @param commandSenderClazz Class of the command sender
     * @apiNote See {@link CommandProperties#excludedSenders()} for usage of these registered classes
     */
    void register(Class<?> commandSenderClazz);

    /**
     * Unregisters the given class from the possible list of command senders
     * @param commandSenderClazz Class of the command sender
     */
    void unregister(Class<?> commandSenderClazz);

    /**
     * @param commandSenderClazz Class of the sende to check
     * @return True if registered, false otherwise
     */
    default boolean isRegistered(Class<?> commandSenderClazz) {
        return getCommandSenders().stream()
                .anyMatch(commandSenderClazz::isAssignableFrom);
    }

    /**
     * @param commandSenderClassName Name of the class, eg. "Player"
     * @return True if registered, false otherwise
     */
    default boolean isRegistered(String commandSenderClassName) {
        return getCommandSenders().stream()
                .anyMatch((cs) -> commandSenderClassName.equalsIgnoreCase(cs.getSimpleName()));
    }

    /**
     * @return Registered command sender classes
     */
    Set<Class<?>> getCommandSenders();

}
