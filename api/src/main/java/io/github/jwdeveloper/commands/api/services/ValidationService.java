package io.github.jwdeveloper.commands.api.services;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;

public interface ValidationService {
    ActionResult<Command> validateCommand(Command command, Object sender, String[] args);
}
