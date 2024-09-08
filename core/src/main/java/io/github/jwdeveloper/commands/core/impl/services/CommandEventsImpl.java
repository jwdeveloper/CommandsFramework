package io.github.jwdeveloper.commands.core.impl.services;

import io.github.jwdeveloper.commands.api.data.events.CommandEvent;
import io.github.jwdeveloper.commands.api.functions.CommandEventInvoker;
import io.github.jwdeveloper.dependance.injector.api.annotations.Inject;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.services.CommandEvents;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.*;
import java.util.function.Function;

@Log
@Getter
public class CommandEventsImpl implements CommandEvents {
    private final Map<Class, List<CommandEventInvoker>> executeEvents;
    private final Map<String, List<Function>> genericsEventMap;

    @Inject
    public CommandEventsImpl() {
        genericsEventMap = new HashMap<>();
        this.executeEvents = new HashMap<>();
    }

    @Override
    public CommandEvents onCommandInvoked(Class senderType, CommandEventInvoker action) {
        executeEvents.computeIfAbsent(senderType, k -> new ArrayList<>()).add(action);
        return this;
    }

    public <I, O> CommandEvents onEvent(String label, Function<I, O> action) {
        genericsEventMap.computeIfAbsent(label, k -> new ArrayList<>()).add(action);
        return this;
    }

    public ActionResult<CommandEvent> publishCommandInvoked(CommandEvent event) {
        for (var entry : executeEvents.entrySet()) {
            var keyType = entry.getKey();
            var actions = entry.getValue();
            var senderType = event.sender().getClass();

            if (!keyType.isAssignableFrom(senderType) && keyType != Object.class) {
                continue;
            }
            for (var action : actions) {
                action.execute(event);
            }
        }
        return ActionResult.success(event);
    }

    public <Input, Output> ActionResult<Output> invoke(String label, Input event, Class<? extends Output> outputType) {

        if (label == null || label.isEmpty()) {
            throw new RuntimeException("Label cannot be null or empty");
        }

        if (!genericsEventMap.containsKey(label)) {
            return ActionResult.failed("There is no event registered for given event label");
        }
        var actions = genericsEventMap.get(label);

        Object output = null;
        for (var action : actions) {
            output = action.apply(event);
        }

        if (output == null) {
            return ActionResult.failed();
        }
        if (!output.getClass().isAssignableFrom(outputType)) {
            return ActionResult.failed("The output event type is " + output.getClass() + " but should be " + outputType);
        }
        return ActionResult.success((Output) output);
    }

}
