package io.github.jwdeveloper.commands.minestom.impl;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.services.ValidationService;
import net.minestom.server.command.builder.parser.ArgumentParser;

public class MinestomValidationService implements ValidationService {
    @Override
    public ActionResult<Command> validateCommand(Command command, Object sender, String[] args) {


        if (args == null) {
            return ActionResult.failed("Arguments can not be null");
        }

        //TODO general command validation

        return ActionResult.success();
    }
}
