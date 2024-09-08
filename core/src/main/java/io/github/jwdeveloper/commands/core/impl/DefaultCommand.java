package io.github.jwdeveloper.commands.core.impl;

import io.github.jwdeveloper.commands.api.data.events.CommandEvent;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.CommandProperties;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import io.github.jwdeveloper.commands.api.data.events.CommandErrorEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandEventImpl;
import io.github.jwdeveloper.commands.api.data.expressions.ArgumentNode;
import io.github.jwdeveloper.commands.core.impl.services.CommandServices;
import io.github.jwdeveloper.commands.core.impl.services.CommandEventsImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public class DefaultCommand implements Command {

    private final DependanceContainer container;
    private final CommandProperties properties;
    private final List<ArgumentProperties> arguments;
    private final List<Command> children;
    private final Map<String, Command> childrenByName;
    private final CommandServices commandService;
    private final CommandEventsImpl eventsService;

    @Setter
    private Command parent;

    public DefaultCommand(CommandProperties properties,
                          List<ArgumentProperties> argumentProperties,
                          List<Command> children,
                          CommandServices services,
                          CommandEventsImpl eventsService,
                          DependanceContainer dependanceContainer) {
        this.properties = properties;
        this.arguments = argumentProperties;
        this.commandService = services;
        this.children = children;
        this.eventsService = eventsService;
        this.childrenByName = children.stream().collect(Collectors.toMap(Command::name, e -> e));
        this.container = dependanceContainer;
    }

    public <S> ActionResult<CommandEvent> executeCommand(S sender, String commandLabel, String... arguments) {
        try {
            var result = commandService.execute(this, sender, commandLabel, arguments);
            var event = result.getValue();

            if (result.isFailed() && event == null) {
                return ActionResult.failed();
            }

            var failed = result.isFailed();
            var resultMessage = result.getMessage();
            List<ArgumentNode> args = event.arguments();
            for (ArgumentNode arg : args) {
                if (arg.isValid()) {
                    continue;
                }
                failed = true;
                resultMessage = arg.getValidationMessage();
            }


            var finishEvent = result.getValue();

            eventsService.invoke("onFinish", finishEvent, Object.class);
            if (!failed) {
                return result;
            }
            return ActionResult.failed(event, resultMessage);
        } catch (Exception e) {
            dispatchError(sender, e, arguments);
            return ActionResult.failed(e.getMessage());
        }
    }



    @Override
    public ActionResult<List<String>> executeSuggestions(Object sender, String alias, String... args) {
        try {
            var result = commandService.executeTab(this, sender, alias, args);
            return ActionResult.success(result);
        } catch (Exception e) {
            dispatchError(sender, e, args);
            return ActionResult.failed(e.getMessage());
        }
    }

    @Override
    public Optional<Command> parent() {
        return Optional.ofNullable(parent);
    }

    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public String name() {
        return properties.name();
    }

    @Override
    public DependanceContainer container() {
        return container;
    }

    @Override
    public boolean hasChild(String name) {
        return childrenByName.containsKey(name);
    }

    @Override
    public Optional<Command> child(String name) {
        return Optional.ofNullable(childrenByName.get(name));
    }


    public void dispatchError(Object sender, Exception e, String... arguments) {
        var event = new CommandErrorEvent(sender, this, e, arguments, e.getMessage());
        dispatchError(event);
    }

    @Override
    public void dispatchError(CommandErrorEvent event) {
        eventsService.invoke(CommandErrorEvent.class.getSimpleName(), event, void.class);
    }
}
