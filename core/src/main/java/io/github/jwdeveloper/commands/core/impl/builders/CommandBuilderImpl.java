package io.github.jwdeveloper.commands.core.impl.builders;

import io.github.jwdeveloper.commands.api.functions.CommandEventInvoker;
import io.github.jwdeveloper.commands.core.impl.services.*;

import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentsTypesRegistry;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import io.github.jwdeveloper.commands.api.services.ActionsRegistry;
import io.github.jwdeveloper.commands.core.impl.DefaultCommand;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.data.CommandProperties;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import io.github.jwdeveloper.commands.api.data.events.CommandErrorEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandEventImpl;
import io.github.jwdeveloper.commands.api.data.events.CommandValidationEvent;
import io.github.jwdeveloper.dependance.decorator.api.annotations.ProxyProvider;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;


@Accessors(fluent = true)
public class CommandBuilderImpl implements CommandBuilder {


    @ProxyProvider
    private CommandBuilder proxy;

    @Getter
    private CommandProperties properties;
    private final CommandEventsImpl events;
    private final CommandsRegistry commands;
    private final DependanceContainer container;
    private final Map<String, CommandBuilder> subCommadnsBuilders;
    private final Map<String, ArgumentBuilderImpl> argumentBuilders;
    private final ArgumentsTypesRegistry argumentTypesRegistry;
    private final Map<Class, String> registeredActionsMap;

    public CommandBuilderImpl(CommandsRegistry registry,
                              DependanceContainer container,
                              ArgumentsTypesRegistry argumentTypesRegistry) {
        this.properties = new CommandProperties();
        this.events = new CommandEventsImpl();
        this.argumentBuilders = new TreeMap<>();
        this.subCommadnsBuilders = new TreeMap<>();
        this.registeredActionsMap = new HashMap<>();
        this.container = container;
        this.commands = registry;
        this.argumentTypesRegistry = argumentTypesRegistry;
    }
//
//    @Override
//    public CommandBuilder withProperties(Consumer<CommandProperties> action) {
//        action.accept(properties);
//        return this;
//    }

//
//    @Override
//    public CommandBuilder onError(Consumer<CommandErrorEvent<?>> action) {
//        events.onCommandError(action);
//        return this;
//    }
//
//    @Override
//    public CommandBuilder onFinished(Consumer<CommandEvent> action) {
//        events.onCommandFinished(action);
//        return this;
//    }

//
//    @Override
//    public CommandBuilder onValidation(Function<CommandValidationEvent, ActionResult<String>> action) {
//        events.onCommandValidation(action);
//        return this;

    @Override
    public Object onExecute(Class executorType, CommandEventInvoker action) {
        events.onCommandInvoked(executorType, action);
        return self();
    }

    @Override
    public Object onAction(String actionName, Class actionType) {
        return self();
    }



    @Override
    public Command register() {
        var command = build();
        commands.add(command);
        return command;
    }


    @Override
    public CommandBuilder addSubCommand(String name) {
        var builder = subCommadnsBuilders.computeIfAbsent(name, s -> container.find(CommandBuilder.class));
        builder.properties().name(name);
        return builder;
    }

    @Override
    public CommandBuilder addSubCommand(String pattern, Consumer builderAction) {
        var builder = addSubCommand(pattern);
        builderAction.accept(builder);
        return self();
    }


    @Override
    public ArgumentBuilder argument(String name) {

        if (argumentBuilders.containsKey(name)) {
            return argumentBuilders.get(name);
        }

        var builder = new ArgumentBuilderImpl(new ArgumentProperties(), argumentTypesRegistry);
        argumentBuilders.put(name, builder);

        builder.withName(name);
        builder.withIndex(argumentBuilders.size());
        return builder;
    }


    @Override
    public CommandBuilder addArgument(String name, Consumer builder) {
        builder.accept(argument(name));
        return self();
    }

    @Override
    public Command build() {

        var arguments = argumentBuilders.values()
                .stream()
                .sorted(Comparator.comparingInt(a -> a.getProperties().index()))
                .map(ArgumentBuilderImpl::build)
                .toList();

        var children = subCommadnsBuilders.values()
                .stream()
                .map(CommandBuilder::build)
                .toList();

        //This looks ugly as fuck
        var actions = container.find(ActionsRegistry.class);
       /* actions.find(CommandValidationEvent.class).ifPresent(action ->
        {
            events.getValidationEvents().add(action.function());
        });
        actions.find(CommandEventImpl.class).ifPresent(action ->
        {
            events.getFinishedEvents().add(event ->
            {
                action.function().apply(event);
            });
        });
        actions.find(CommandErrorEvent.class).ifPresent(action ->
        {
            events.getErrorEvents().add(event ->
            {
                action.function().apply(event);
            });
        });
       */
        actions.find(CommandBuilder.class).ifPresent(action ->
        {
            action.function().apply(this);
        });

        var commandContainer = container.createChildContainer()
                .registerSingleton(CommandEventsImpl.class, events)
                .registerSingleton(CommandServices.class)
                .registerSingleton(DefaultValidationService.class)
                .registerSingleton(ExpressionService.class)
                .registerSingleton(CommandParser.class)
                .registerSingleton(Command.class, con ->
                {
                    var container = (DependanceContainer) con.find(DependanceContainer.class);
                    var services = (CommandServices) con.find(CommandServices.class);
                    var events = (CommandEventsImpl) con.find(CommandEventsImpl.class);
                    return new DefaultCommand(
                            properties,
                            arguments,
                            children,
                            services,
                            events,
                            container);
                })
                .build();
        return commandContainer.find(Command.class);
    }

    @Override
    public CommandBuilder self() {
        if (proxy != null)
            return proxy;

        return this;
    }

    @Override
    public Object withProperties(Consumer properties) {
        return null;
    }

}
