package io.github.jwdeveloper.commands.core.impl;

import io.github.jwdeveloper.commands.api.services.CommandsSendersRegistry;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import io.github.jwdeveloper.commands.api.TemplateParser;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentsTypesRegistry;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.services.ActionsRegistry;
import io.github.jwdeveloper.commands.core.impl.patterns.PatternBuilderVisitor;
import io.github.jwdeveloper.commands.api.patterns.PatternsRegistry;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Getter
@Accessors(fluent = true)
public class DefaultCommands implements Commands {
    private final PatternBuilderVisitor patternService;
    private final TemplateParser commandsTemplate;
    private final ActionsRegistry actions;
    private final CommandsRegistry commandsRegistry;
    private final PatternsRegistry patterns;
    private final DependanceContainer container;
    private final ArgumentsTypesRegistry argumentTypes;
    private final CommandsSendersRegistry commandSenders;

    public DefaultCommands(CommandsRegistry commandsRegistry,
                           DependanceContainer container,
                           ArgumentsTypesRegistry argumentTypesRegistry,
                           TemplateParser commandsTemplate,
                           PatternBuilderVisitor patternService,
                           ActionsRegistry actionsRegistry,
                           PatternsRegistry patterns,
                           CommandsSendersRegistry commandSenders) {
        this.patterns = patterns;
        this.commandsRegistry = commandsRegistry;
        this.container = container;
        this.commandsTemplate = commandsTemplate;
        this.patternService = patternService;
        this.argumentTypes = argumentTypesRegistry;
        this.actions = actionsRegistry;
        this.commandSenders = commandSenders;
    }

    @Override
    public CommandBuilder create(String pattern) {
        var result = patternService.getCommandBuilder(this, pattern, create());
        if (result.isFailed()) {
            throw new RuntimeException(result.getMessage());
        }
        return result.getValue();
    }

    public CommandBuilder create() {
        return container.find(CommandBuilder.class);
    }

    @Override
    public CommandBuilder create(Object templateObject) {
        return commandsTemplate.templateToBuilder(templateObject, create());
    }

    @Override
    public void register(Command command) {
        commandsRegistry.add(command);
    }

    @Override
    public void unregister(Command command) {
        commandsRegistry.remove(command);
    }

    @Override
    public void removeAll() {
        commandsRegistry.removeAll();
    }

    @Override
    public List<Command> findAll() {
        return commandsRegistry.commands();
    }

    @Override
    public Stream<Command> findBy(Predicate<Command> predicate) {
        return commandsRegistry.commands().stream().filter(predicate);
    }

}
