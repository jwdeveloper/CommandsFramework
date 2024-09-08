package io.github.jwdeveloper.commands.api.builders;

import io.github.jwdeveloper.commands.api.functions.CommandEventInvoker;

/**
 * Interface for building command events in a Bukkit plugin.
 *
 * @param <SELF> The type of the builder implementing this interface.
 */
public interface CommandEventsBuilder<SELF, EXECUTOR> {

    /**
     * Registers a custom event action for a specific type of command sender.
     *
     * @param executorType The class of the command sender.
     * @param action       The action to be executed.
     * @return The builder instance.
     */
    SELF onExecute(Class<? extends EXECUTOR> executorType, CommandEventInvoker<? extends EXECUTOR> action);


    /**
     * Executes on any sender type
     *
     * @param action
     * @return
     */
    default SELF onExecute(CommandEventInvoker<? extends EXECUTOR> action) {
        return onExecute((Class<? extends EXECUTOR>) Object.class, action);
    }

    /**
     * Register an action from the ActionsRegistry to command event
     *
     * @param actionName the action name
     * @param actionType the action input type
     * @param <E>        The action input type
     * @return The builder instance.
     */
    <E> SELF onAction(String actionName, Class<? extends E> actionType);

    /*
     */
/**
 * Registers an event action to be executed when a command is run by a ProxiedCommandSender.
 *
 * @param action The action to be executed.
 * @return The builder instance.
 *//*

    SELF onError(Consumer<CommandErrorEvent<? extends EXECUTOR>> action);

    default SELF onError(String actionIdentifier) {
        return onAction(actionIdentifier, CommandErrorEvent.class);
    }


    */
/**
 * Invoked after command execution is finished
 *
 * @param action The action to be executed.
 * @return The builder instance.
 *//*

    SELF onFinished(Consumer<CommandEvent<? extends EXECUTOR>> action);

    default SELF onFinished(String actionIdentifier) {
        return onAction(actionIdentifier, CommandEvent.class);
    }


    */
/**
 * Registers a validation action to be executed when a command is validated.
 *
 * @param action The action to be executed.
 * @return The builder instance.
 *//*

    SELF onValidation(Function<CommandValidationEvent<? extends EXECUTOR>, ActionResult<String>> action);

    default SELF onValidation(String actionIdentifier) {
        return onAction(actionIdentifier, CommandValidationEvent.class);
    }
*/


}
