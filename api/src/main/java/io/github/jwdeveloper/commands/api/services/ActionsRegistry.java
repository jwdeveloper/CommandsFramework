package io.github.jwdeveloper.commands.api.services;

import io.github.jwdeveloper.commands.api.builders.CommandBuilder;

import io.github.jwdeveloper.commands.api.data.ActionData;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.*;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ActionsRegistry {

    ActionResult<ActionData> find(String identifier, Class inputType);

    ActionResult<ActionData> find(Class inputType);

    <I, O> ActionsRegistry register(String identifier, Class<? extends I> eventClazz, Function<I, O> action);


    default ActionsRegistry onValidation(Function<CommandValidationEvent, Boolean> action) {
        return onValidation("", action);
    }

    default ActionsRegistry onValidation(String identifier, Function<CommandValidationEvent, Boolean> action) {
        return register(identifier, CommandValidationEvent.class, action);
    }

    default ActionsRegistry onError(Consumer<CommandErrorEvent> action) {
        return onError("", action);
    }

    default ActionsRegistry onError(String identifier, Consumer<CommandErrorEvent> action) {
        return register(identifier, CommandErrorEvent.class, (e) ->
        {
            action.accept(e);
            return null;
        });
    }

    default ActionsRegistry onBuilder(Consumer<CommandBuilder> action) {
        return onBuilder("", action);
    }

    default ActionsRegistry onBuilder(String identifier, Consumer<CommandBuilder> action) {
        return register(identifier, CommandBuilder.class, (e) ->
        {
            action.accept(e);
            return null;
        });
    }

    default ActionsRegistry onFinalize(Consumer<CommandEventImpl> action) {
        return onFinalize("", action);
    }

    default ActionsRegistry onFinalize(String identifier, Consumer<CommandEventImpl> action) {
        return register(identifier, CommandEventImpl.class, (e) ->
        {
            action.accept(e);
            return null;
        });
    }

    default ActionsRegistry onArgumentParse(Function<ArgumentParseEvent, Boolean> action) {
        return onArgumentParse("", action);
    }

    default ActionsRegistry onArgumentParse(String identifier, Function<ArgumentParseEvent, Boolean> action) {
        return register(identifier, ArgumentParseEvent.class, action);
    }

    default ActionsRegistry onArgumentSuggestions(Function<ArgumentSuggestionEvent, Boolean> action) {
        return onArgumentSuggestions("", action);
    }

    default ActionsRegistry onArgumentSuggestions(String identifier, Function<ArgumentSuggestionEvent, Boolean> action) {
        return register(identifier, ArgumentSuggestionEvent.class, action);
    }

}
