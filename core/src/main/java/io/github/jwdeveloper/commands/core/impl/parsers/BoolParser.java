package io.github.jwdeveloper.commands.core.impl.parsers;

import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentType;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;

import java.util.List;

public class BoolParser implements ArgumentType {

    private final List boolValues;

    public BoolParser() {

        boolValues = List.of("True", "False");
    }

    @Override
    public String name() {
        return "Bool";
    }

    @Override
    public void onArgumentBuilder(ArgumentBuilder builder) {
    }

    @Override
    public ActionResult<List<String>> onSuggestion(ArgumentSuggestionEvent event) {
        return ActionResult.success(boolValues);
    }

    @Override
    public ActionResult<Object> onParse(ArgumentParseEvent event) {
        var input = event.nextArgument();
        if (!input.equalsIgnoreCase("True") && !input.equalsIgnoreCase("True"))
            ActionResult.failed("value should be True or False");
        return ActionResult.success(Boolean.parseBoolean(input));
    }


}
