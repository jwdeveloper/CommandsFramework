package io.github.jwdeveloper.commands.api.functions;

import io.github.jwdeveloper.commands.api.data.events.CommandEvent;


/**
 * The functional interface used for being triggered when command onInvoke event
 *
 * @param <EXECUTOR> the type of the object that is invoking a command
 */
@FunctionalInterface
public interface CommandEventInvoker<EXECUTOR> {
    void execute(CommandEvent<? extends EXECUTOR> event);
}
