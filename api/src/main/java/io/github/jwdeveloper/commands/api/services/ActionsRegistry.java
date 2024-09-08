package io.github.jwdeveloper.commands.api.services;

import io.github.jwdeveloper.commands.api.builders.CommandBuilder;

import io.github.jwdeveloper.commands.api.data.ActionData;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.*;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ActionsRegistry {

    /**
     * Register action that will be invoked by specified event
     *
     *
     * @param identifier the action label identifier
     * @param eventName  the name of the event
     * @param action     the lambda action
     * @param <I>        The action input type
     * @param <O>        The action output type
     * @return self instance
     */
    <I, O> ActionsRegistry register(String identifier, String eventName, Function<I, O> action);

    ActionResult<ActionData> find(String identifier, Class inputType);

    ActionResult<ActionData> find(Class inputType);


}
