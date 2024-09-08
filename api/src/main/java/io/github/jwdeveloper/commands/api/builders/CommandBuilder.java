package io.github.jwdeveloper.commands.api.builders;

import io.github.jwdeveloper.commands.api.Command;

/**
 * Command builder is used for creating new commands
 * as you can see itself it does not have many methods.
 * Therefor to see its features you need to jump to interfaces
 * that it extends
 */

/**
 * @param <SELF>   type of the builder that extends CommandBuilder interface
 * @param <SENDER> type of the sender object used in the implementation of the command builder
 */
public interface CommandBuilder<SELF extends CommandBuilder<SELF, SENDER>, SENDER> extends
        CommandEventsBuilder<SELF, SENDER>,
        CommandPropsBuilder<SELF>,
        CommandArgumentsBuilder<SELF>,
        SubCommandsBuilder<SELF> {

    /**
     * Builds and register the command
     *
     * @return the command
     */
    Command register();

    /**
     * Builds the command
     *
     * @return the command
     */
    Command build();
    SELF self();
}
