package io.github.jwdeveloper.commands.core.impl.services;

import io.github.jwdeveloper.commands.api.data.events.*;

import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.services.ActionsRegistry;
import io.github.jwdeveloper.commands.core.impl.data.Ref;
import io.github.jwdeveloper.commands.api.functions.ArgumentParser;
import io.github.jwdeveloper.commands.api.functions.ArgumentSuggestions;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


public class ActionBindingService {
    private final DependanceContainer container;
    private final ActionsRegistry actionsRegistry;

    public ActionBindingService(DependanceContainer dependanceContainer, ActionsRegistry actionsRegistry) {
        this.container = dependanceContainer;
        this.actionsRegistry = actionsRegistry;
    }


    public <Input, Output> Function<Input, Output> bindAction(String key, Class<? extends Input> actionType, Object target) {
        var isMethod = key.startsWith("@");
        Object invokable = null;
        if (isMethod) {
            invokable = getMethod(target, key);
        } else {
            invokable = actionsRegistry.find(key, actionType);
        }
        return null;
    }


    public Function<CommandValidationEvent, ActionResult<String>> bindValidationMethod(Object target, String methodName) {

        var method = getMethod(target, methodName);
        var invoker = getInvoker(method, target);

        var eventRef = new Ref<>();
        var senderRef = new Ref<>();
        var commandRef = new Ref<>();
        var argsRef = new Ref<>();
        var methodContainer = container.createChildContainer()
                .registerTransient(String[].class, con -> argsRef.getValue())
                //.registerTransient(CommandSender.class, con -> senderRef.getValue())
                .registerTransient(Command.class, con -> commandRef.getValue())
                .registerTransient(CommandValidationEvent.class, con -> eventRef.getValue())
                .build();

        return event ->
        {
            try {
                eventRef.setValue(event);
                argsRef.setValue(event.args());
                senderRef.setValue(event.sender());
                commandRef.setValue(event.command());
                var arguments = methodContainer.resolveParameters(method);
                var result = method.invoke(invoker, arguments);
                if (result instanceof Boolean bool) {
                    if (bool)
                        return ActionResult.success();
                    else
                        return ActionResult.failed();
                }
                if (result instanceof String msg) {
                    return ActionResult.success(msg);
                }
                if (result instanceof ActionResult actionResult) {
                    return actionResult.cast();
                }
                return ActionResult.success(result.toString());
            } catch (Exception e) {
                var error = new CommandErrorEvent(event.sender(), event.command(), e, new String[1], e.getMessage());
                event.command().dispatchError(error);
                return ActionResult.failed(e.getMessage());
            }
        };
    }

    public Consumer<CommandEventImpl> bindFinishedMethod(Object target, String methodName) {
        var method = getMethod(target, methodName);
        var invoker = getInvoker(method, target);

        var action = actionsRegistry.find(methodName, CommandEventImpl.class);

        var eventRef = new Ref<>();
        var senderRef = new Ref<>();
        var commandRef = new Ref<>();
        var methodContainer = container.createChildContainer()
               // .registerTransient(CommandSender.class, con -> senderRef.getValue())
                .registerTransient(Command.class, con -> commandRef.getValue())
                .registerTransient(CommandEventImpl.class, con -> eventRef.getValue())
                .build();

        return event ->
        {
            try {
                eventRef.setValue(event);
                senderRef.setValue(event.sender());
                commandRef.setValue(event.command());
                var arguments = methodContainer.resolveParameters(method);
                method.invoke(invoker, arguments);
            } catch (Exception e) {
                var error = new CommandErrorEvent(event.sender(), event.command(), e, new String[1], "XXX" + e.getMessage());
                event.command().dispatchError(error);
            }
        };
    }

    public Consumer<CommandErrorEvent> bindErrorMethod(Object target, String methodName) {

        var method = getMethod(target, methodName);
        var invoker = getInvoker(method, target);

        var eventRef = new Ref<>();
        var senderRef = new Ref<>();
        var commandRef = new Ref<>();
        var methodContainer = container.createChildContainer()
               // .registerTransient(CommandSender.class, con -> senderRef.getValue())
                .registerTransient(Command.class, con -> commandRef.getValue())
                .registerTransient(CommandErrorEvent.class, con -> eventRef.getValue())
                .build();

        return event ->
        {
            //Avoiding recursion
            if (event.errorMessage() != null && event.errorMessage().contains("XXX")) {
                return;
            }

            try {
                eventRef.setValue(event);
                senderRef.setValue(event.sender());
                commandRef.setValue(event.command());
                var arguments = methodContainer.resolveParameters(method);
                method.invoke(invoker, arguments);
            } catch (Exception e) {
                var error = new CommandErrorEvent(event.sender(), event.command(), e, new String[1], "XXX" + e.getMessage());
                event.command().dispatchError(error);
            }
        };
    }

    public ArgumentParser bindParseMethod(Object target, String methodName) {
        var method = getMethod(target, methodName);
        var invoker = getInvoker(method, target);

        var eventRef = new Ref<>();
        var senderRef = new Ref<>();
        var commandRef = new Ref<>();
        var methodContainer = container.createChildContainer()
                //.registerTransient(CommandSender.class, con -> senderRef.getValue())
                .registerTransient(Command.class, con -> commandRef.getValue())
                .registerTransient(ArgumentParseEvent.class, con -> eventRef.getValue())
                .build();

        return (event) ->
        {
            try {
                eventRef.setValue(event);
                senderRef.setValue(event.sender());
                commandRef.setValue(event.command());
                var arguments = methodContainer.resolveParameters(method);
                var methodResult = method.invoke(invoker, arguments);
                return ActionResult.success(methodResult);
            } catch (Exception e) {
                var error = new CommandErrorEvent(event.sender(), event.command(), e, new String[1], e.getMessage());
                event.command().dispatchError(error);
                return ActionResult.failed(e.getMessage());
            }
        };
    }

    public ArgumentSuggestions bindSuggestionsMethod(Object target, String methodName) {
        var method = getMethod(target, methodName);
        var invoker = getInvoker(method, target);


        var eventRef = new Ref<>();
        var senderRef = new Ref<>();
        var commandRef = new Ref<>();
        var methodContainer = container.createChildContainer()
                //.registerTransient(CommandSender.class, con -> senderRef.getValue())
                .registerTransient(Command.class, con -> commandRef.getValue())
                .registerTransient(ArgumentSuggestionEvent.class, con -> eventRef.getValue())
                .build();

        return (event) ->
        {
            try {
                eventRef.setValue(event);
                senderRef.setValue(event.sender());
                commandRef.setValue(event.command());
                var arguments = methodContainer.resolveParameters(method);
                var methodResult = (List<String>) method.invoke(invoker, arguments);
                return ActionResult.success(methodResult);
            } catch (Exception e) {
                var error = new CommandErrorEvent(event.sender(), event.command(), e, new String[1], e.getMessage());
                event.command().dispatchError(error);
                return ActionResult.failed(e.getMessage());
            }
        };
    }

    private Object getInvoker(Method method, Object target) {
        if (Modifier.isStatic(method.getModifiers()))
            return null;
        else
            return target;
    }

    public Method getMethod(Object target, String methodName) {
        if (methodName.endsWith("()"))
            methodName = methodName.replace("()", "");


        var finalMethodName = methodName;
        var method = Arrays.stream(target.getClass().getDeclaredMethods())
                .filter(e -> e.getName().equalsIgnoreCase(finalMethodName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("method not found!"));
        method.setAccessible(true);
        return method;
    }


}
