package io.github.jwdeveloper.commands.api.services;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.DisplayAttribute;

public interface MessagesService {

    default ActionResult<String> displayArgument(ArgumentProperties argument, String error) {
        if (argument.hasDisplayAttribute(DisplayAttribute.NONE)) {
            return ActionResult.failed();
        }
        if (argument.hasDisplayAttribute(DisplayAttribute.SUGGESTIONS)) {
            return ActionResult.failed();
        }

        var message = new StringBuilder();

        if (argument.hasDisplayAttribute(DisplayAttribute.NAME)) {
            message.append("<");
            message.append(argument.name());
            if (argument.hasDisplayAttribute(DisplayAttribute.TYPE)) {
                message.append(":");
            }
        }
        if (argument.hasDisplayAttribute(DisplayAttribute.TYPE)) {
            message.append(argument.type());
        }
        if (argument.hasDisplayAttribute(DisplayAttribute.DESCRIPTION)) {
            message.append(" [").append(argument.description()).append("]");
        }
        if (argument.hasDisplayAttribute(DisplayAttribute.ERROR) && error != null && !error.isBlank()) {
            message.append(" ! ").append(error);
        }

        if (argument.hasDisplayAttribute(DisplayAttribute.NAME) && error != null) {
            message.append(">");
        }
        return ActionResult.success(message.toString());
    }


    default String inactiveCommand(String name) {
        return "command is inactive " + name;
    }

    default String invalidArgument(String validationMessage) {
        return "invalid arguments " + validationMessage;
    }

    default String insufficientPermissions(String permission) {
        return "insufficient permissions: " + permission;
    }

    default String noSenderAccess(String message) {
        return "The command can not be invoked by: " + message;
    }
}
