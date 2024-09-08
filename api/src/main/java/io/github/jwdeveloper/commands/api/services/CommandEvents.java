package io.github.jwdeveloper.commands.api.services;

import io.github.jwdeveloper.commands.api.data.events.CommandEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandEventImpl;

import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.CommandErrorEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandValidationEvent;
import io.github.jwdeveloper.commands.api.functions.CommandEventInvoker;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The class Manage all possible events fires during a command invocation
 */
public interface CommandEvents {
    /**
     * Event fires when command got invoked,
     * the invocation arguments need to be valid this event got invoked
     *
     * @param senderType specify on what kind of SenderType the event should be triggered
     *                   CommandSender or class that inherits from it.
     * @param action     the event action
     */
    CommandEvents onCommandInvoked(Class senderType, CommandEventInvoker action);


    /**
     * @param label  The tag that you can use to call certain event
     * @param action the lambda action
     * @param <I>    Input event action type
     * @param <O>    Output event action type
     */
    <I, O> CommandEvents onEvent(String label, Function<I, O> action);


    /**
     * Events fires when command arguments is getting validated
     * This event is invoked before player permission
     *
     * @param action the event action
     */
    default CommandEvents onCommandValidation(Function<CommandValidationEvent, ActionResult<String>> action) {
        return onEvent(CommandValidationEvent.class.getSimpleName(), action);
    }

    /**
     * Event fires when an error or exception was thrown during command invocation
     * When event is not set, by the default exception is displayed to console
     *
     * @param action the event action
     */
    default CommandEvents onCommandError(Function<CommandErrorEvent, ActionResult<String>> action) {
        return onEvent(CommandErrorEvent.class.getSimpleName(), action);
    }


    /**
     * Event fires after onCommandInvoked event
     * You can use it for the postprocessing of command output
     *
     * @param action the event action
     */
    default CommandEvents onCommandFinished(Consumer<CommandEvent> action) {
        return onEvent(CommandEvent.class.getSimpleName(), commandEvent ->
        {
            action.accept((CommandEvent) commandEvent);
            return null;
        });
    }

}
