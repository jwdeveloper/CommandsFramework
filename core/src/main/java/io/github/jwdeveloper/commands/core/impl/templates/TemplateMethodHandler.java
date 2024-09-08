package io.github.jwdeveloper.commands.core.impl.templates;

import io.github.jwdeveloper.dependance.api.DependanceContainer;

import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.events.CommandErrorEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandEventImpl;
import io.github.jwdeveloper.commands.api.data.expressions.CommandNode;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TemplateMethodHandler {

    private final Object target;
    private final Method method;
    private final DependanceContainer container;
    private CommandEventImpl commandEvent;
    private Map<Class<?>, Integer> typeCounter;

    public TemplateMethodHandler(Object target, Method method, DependanceContainerBuilder containerBuilder) {
        this.container = createContainer(containerBuilder);
        this.target = target;
        this.method = method;
        typeCounter = new HashMap<>();
    }

    public void invokeEvent(CommandEventImpl event) {
        this.commandEvent = event;
        typeCounter.clear();
        try {
            var params = container.resolveParameters(method);
            var result = method.invoke(target, params);
            event.output(result);
        } catch (Exception e) {
            var error = new CommandErrorEvent(event.sender(), event.command(), e, new String[1], e.getMessage());
            event.command().dispatchError(error);
        }
    }


    private DependanceContainer createContainer(DependanceContainerBuilder builder) {
        builder.registerSingleton(CommandEventImpl.class, container1 -> commandEvent);
        builder.registerSingleton(Command.class, container1 -> commandEvent.command());
        //builder.registerSingleton(CommandSender.class, container1 -> commandEvent.sender());
        builder.configure(configuration ->
        {
            configuration.onInjection(this::handleArguments);
            configuration.onInjection(this::handleSender);
        });
        return builder.build();
    }

    private Object handleSender(OnInjectionEvent event) {
        /*if (event.input().equals(CommandSender.class)) {
            return event.output();
        }
        if (CommandSender.class.isAssignableFrom(event.input())) {
            return event.container().find(CommandSender.class);
        }*/
        return event.output();
    }

    private Object handleArguments(OnInjectionEvent event) {

        if (event.output() != null) {
            return event.output();
        }


        typeCounter.putIfAbsent(event.input(), -1);
        var index = typeCounter.get(event.input()) + 1;


        typeCounter.put(event.input(), index);

        var value = getArgumentByType(event.input(), index);
        return value;
    }

    private Object getArgumentByType(Class type, int index) {
        List<CommandNode> commands = commandEvent.commands();
        var argIndex = 0;
        for (var cmd : commands) {
            for (var arg : cmd.getArguments()) {
                var value = arg.getValue();
                if (value == null) {
                    continue;
                }
                if (value instanceof Double doubleValue) {
                    value = switch (type.getSimpleName()) {
                        case "int", "Integer" -> (int) doubleValue.intValue();
                        case "long", "Long" -> (long) doubleValue.longValue();
                        case "float", "Float" -> (float) doubleValue.floatValue();
                        case "double", "Double" -> doubleValue;
                        case "short", "Short" -> (short) doubleValue.shortValue();
                        case "byte", "Byte" -> (byte) doubleValue.byteValue();
                        default -> value;
                    };

                    value = convertToPrimitive(value);
                }

                if (!type.equals(value.getClass())) {
                    //I hate java
                    if (value instanceof Integer && type.equals(int.class)) {
                        argIndex++;
                        return value;
                    }
                    if (value instanceof Double && type.equals(double.class)) {
                        argIndex++;
                        return value;
                    }
                    if (value instanceof Float && type.equals(float.class)) {
                        argIndex++;
                        return value;
                    }
                    if (value instanceof Long && type.equals(long.class)) {
                        argIndex++;
                        return value;
                    }
                    if (value instanceof Short && type.equals(short.class)) {
                        argIndex++;
                        return value;
                    }
                    if (value instanceof Byte && type.equals(byte.class)) {
                        argIndex++;
                        return value;
                    }
                    continue;
                }

                if (index == argIndex) {
                    return value;
                }
                argIndex++;
            }
        }
        throw new RuntimeException("Argument not found! " + type.getSimpleName() + " with index " + index);
    }

    public static Object convertToPrimitive(Object input) {
        if (input instanceof Integer) {
            return ((Integer) input).intValue();
        } else if (input instanceof Double) {
            return ((Double) input).doubleValue();
        } else if (input instanceof Boolean) {
            return ((Boolean) input).booleanValue();
        } else if (input instanceof Character) {
            return ((Character) input).charValue();
        } else if (input instanceof Long) {
            return ((Long) input).longValue();
        } else if (input instanceof Float) {
            return ((Float) input).floatValue();
        } else if (input instanceof Short) {
            return ((Short) input).shortValue();
        } else if (input instanceof Byte) {
            return ((Byte) input).byteValue();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + input.getClass().getName());
        }
    }
}
