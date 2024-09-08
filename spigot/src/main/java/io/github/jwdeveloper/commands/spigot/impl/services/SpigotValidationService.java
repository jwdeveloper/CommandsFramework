package io.github.jwdeveloper.commands.spigot.impl.services;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.SenderType;
import io.github.jwdeveloper.commands.api.data.events.CommandValidationEvent;
import io.github.jwdeveloper.commands.api.services.MessagesService;
import io.github.jwdeveloper.commands.api.services.ValidationService;
import io.github.jwdeveloper.commands.core.impl.services.CommandEventsImpl;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpigotValidationService implements ValidationService {
    private final MessagesService messages;

    public SpigotValidationService(MessagesService messagesService) {
        this.messages = messagesService;
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

    public ActionResult<Object> isSenderEnabled(Object sender, List<SenderType> senderTypes) {
        for (var accessType : senderTypes) {
            var isDisabled = switch (accessType) {
                case PLAYER -> sender instanceof Player;
                case CONSOLE -> sender instanceof ConsoleCommandSender;
                case PROXY -> sender instanceof ProxiedCommandSender;
                case BLOCK -> sender instanceof BlockCommandSender;
                case REMOTE_CONSOLE -> sender instanceof RemoteConsoleCommandSender;
            };
            if (isDisabled) {
                return ActionResult.failed(accessType.name());
            }
        }
        return ActionResult.success(sender);
    }
}
