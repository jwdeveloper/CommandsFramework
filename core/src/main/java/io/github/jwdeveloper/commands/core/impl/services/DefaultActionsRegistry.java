package io.github.jwdeveloper.commands.core.impl.services;

import io.github.jwdeveloper.commands.api.data.ActionData;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.services.ActionsRegistry;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public class DefaultActionsRegistry implements ActionsRegistry {
    private final Map<String, ActionData> actions = new HashMap<>();

    @Override
    public <I, O> ActionsRegistry register(String identifier, String eventName, Function<I, O> action) {
        var key = getKey(identifier, Object.class);
        actions.computeIfAbsent(key, s ->
        {
            var data = new ActionData();
            data.tag(key);
            data.inputType(Object.class);
            data.function(action);
            return data;
        });
        return this;
    }

    public ActionResult<ActionData> find(String identifier, Class inputType) {
        var key = getKey(identifier, inputType);
        if (!actions.containsKey(key))
            return ActionResult.failed("Action with identifier " + key + " not found!");
        return ActionResult.success(actions.get(key));
    }

    @Override
    public ActionResult<ActionData> find(Class inputType) {
        return find("", inputType);
    }


    private String getKey(String name, Class type) {
        return name + "@" + type.getName();
    }


    private Set<Method> getAllMethod(Class<?> type) {
        var methods = Arrays.stream(type.getMethods()).toList();
        var delcared = Arrays.stream(type.getDeclaredMethods()).toList();

        var set = new HashSet<Method>();
        set.addAll(methods);
        set.addAll(delcared);
        return set;
    }

}
