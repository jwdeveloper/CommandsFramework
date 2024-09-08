package io.github.jwdeveloper.commands.core.impl.builders;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentType;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentTypeBuilder;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentsTypesRegistry;
import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;
import io.github.jwdeveloper.commands.api.functions.ArgumentParser;
import io.github.jwdeveloper.commands.api.functions.ArgumentSuggestions;

import java.util.List;
import java.util.function.Consumer;

public class ArgumentTypeBuilderImpl implements ArgumentTypeBuilder {

    private final String name;
    private final ArgumentsTypesRegistry argumentTypesRegistry;
    private ArgumentParser parser;
    private ArgumentSuggestions suggestions;
    private Consumer<ArgumentBuilder> buildAction;
    private boolean cached;


    public ArgumentTypeBuilderImpl(String name, ArgumentsTypesRegistry argumentTypesRegistry) {
        this.name = name;
        this.argumentTypesRegistry = argumentTypesRegistry;
        this.cached = false;
        suggestions = (x) -> ActionResult.success(List.of());
        parser = (x) -> ActionResult.success(x.iterator().next());
        buildAction = (x) -> {
        };

        var i = 0;
    }

    @Override
    public ArgumentTypeBuilder cache() {
        this.cached = true;
        return this;
    }


    @Override
    public ArgumentTypeBuilder onSuggestionAction(ArgumentSuggestions suggestions) {
        this.suggestions = suggestions;
        return this;
    }

    @Override
    public ArgumentTypeBuilder onParseAction(ArgumentParser parser) {
        this.parser = parser;
        return this;
    }

    @Override
    public ArgumentTypeBuilder onArgumentBuilder(Consumer<ArgumentBuilder> action) {
        this.buildAction = action;
        return this;
    }

    @Override
    public ArgumentType register() {
        var argumentType = new ArgumentType() {
            @Override
            public void onArgumentBuilder(ArgumentBuilder builder) {
                buildAction.accept(builder);
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public ActionResult<List<String>> onSuggestion(ArgumentSuggestionEvent event) {
                return suggestions.onSuggestion(event);
            }

            @Override
            public ActionResult<Object> onParse(ArgumentParseEvent event) {
                return parser.onParse(event);
            }
        };

        argumentTypesRegistry.register(argumentType);
        return argumentType;
    }

}
