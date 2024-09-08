package io.github.jwdeveloper.commands.api.data.expressions;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommandNode {
    private Command command;
    private String[] raw;
    private List<ArgumentNode> arguments = new ArrayList<>();


    public ArgumentNode lastArgument()
    {
        return  arguments.get(arguments.size() - 1);
    }

    public ActionResult<ArgumentNode> getLastResolvedArgument() {
        ArgumentNode argumentNode = null;
        for (var arg : arguments) {
            argumentNode = arg;
        }

        if (argumentNode == null) {
            return ActionResult.failed();
        }

        return ActionResult.success(argumentNode);
    }


    public boolean hasEnded() {
        if (arguments.isEmpty()) {
            return true;
        }
        var lastArg = arguments.get(arguments.size() - 1);
        return lastArg.isEnd();
    }

    public ArgumentNode getArgument(int index) {
        return arguments.get(index);
    }

}
