package io.github.jwdeveloper.commands.spigot.impl.services;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.CommandValidationEvent;
import io.github.jwdeveloper.commands.api.services.CommandSenderRegistry;
import io.github.jwdeveloper.commands.api.services.MessagesService;
import io.github.jwdeveloper.commands.api.services.ValidationService;
import io.github.jwdeveloper.commands.core.impl.services.CommandEventsImpl;
import org.bukkit.entity.Player;

import java.util.List;

public class SpigotValidationService implements ValidationService {
    private final MessagesService messages;
    private final CommandSenderRegistry senders;

    public SpigotValidationService(MessagesService messages, CommandSenderRegistry senders) {
        this.messages = messages;
        this.senders = senders;
    }

    public ActionResult<Command> validateCommand(Command command, Object sender, String[] args) {

        if (!command.properties().active()) {
            return ActionResult.failed(messages.inactiveCommand(command.name()));
        }
        var accessResult = isSenderEnabled(sender, command.properties().excludedSenders());
        if (accessResult.isFailed()) {
            return ActionResult.failed(messages.noSenderAccess(accessResult.getMessage()));
        }

        var permissionResult = hasSenderPermissions(sender, command.properties().permission());
        if (permissionResult.isFailed()) {
            return ActionResult.failed(messages.insufficientPermissions(permissionResult.getMessage()));
        }

        var events = command.container().find(CommandEventsImpl.class);
        var result = events.invoke(
                CommandValidationEvent.class.getSimpleName(),
                new CommandValidationEvent(command, sender, args),
                Boolean.class);

        if (result.isFailed()) {
            return result.cast(command);
        }

        return ActionResult.success(command);
    }

    public ActionResult<Object> hasSenderPermissions(Object sender, String permissions) {
        if (!(sender instanceof Player player)) {
            return ActionResult.success();
        }

        if (permissions == null || permissions.isEmpty() || permissions.isBlank())
            return ActionResult.success();

        if (!player.hasPermission(permissions)) {
            return ActionResult.failed(sender, permissions);
        }

        return ActionResult.success(sender);
    }

    public ActionResult<Object> isSenderEnabled(Object sender, List<Class<?>> excludedSenders) {
        if (!senders.isRegistered(sender.getClass())) return ActionResult.failed(String.format("Unregistered sender %s", sender.getClass().getSimpleName()));

        for (var accessType : excludedSenders) {
            if (sender.getClass().isAssignableFrom(accessType)) {
                return ActionResult.failed(accessType.getSimpleName());
            }
        }
        return ActionResult.success(sender);
    }
}
