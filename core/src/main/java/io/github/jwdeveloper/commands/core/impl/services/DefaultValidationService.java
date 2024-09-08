package io.github.jwdeveloper.commands.core.impl.services;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.services.MessagesService;
import io.github.jwdeveloper.commands.api.services.ValidationService;

import java.util.List;

public class DefaultValidationService implements ValidationService {

    private final MessagesService messages;

    public DefaultValidationService(MessagesService messagesService) {
        this.messages = messagesService;
    }

    public ActionResult<Command> validateCommand(Command command, Object sender, String[] args) {
        return ActionResult.success(command);
    }

}
