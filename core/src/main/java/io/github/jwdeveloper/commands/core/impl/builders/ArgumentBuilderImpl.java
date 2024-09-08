package io.github.jwdeveloper.commands.core.impl.builders;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentsTypesRegistry;
import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.DisplayAttribute;
import io.github.jwdeveloper.commands.api.functions.ArgumentParser;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

public class ArgumentBuilderImpl implements ArgumentBuilder {

    @Getter
    private final ArgumentProperties properties;
    private final ArgumentsTypesRegistry argumentTypes;

    public ArgumentBuilderImpl(ArgumentProperties properties,
                               ArgumentsTypesRegistry argumentTypesRegistry) {
        this.properties = properties;
        argumentTypes = argumentTypesRegistry;
    }

    @Override
    public ArgumentBuilder withProperty(Consumer<ArgumentProperties> action) {
        action.accept(properties);
        return this;
    }

    @Override
    public ArgumentBuilder withParser(ArgumentParser parserType) {
        withProperty(argumentProperties ->
        {
            argumentProperties.parser(parserType);
        });
        return this;
    }

    @Override
    public ArgumentBuilder withType(String name) {
        properties.type(name);
        var argumentType = argumentTypes
                .findByName(name)
                .orElseThrow(() -> new RuntimeException("ArgumentType not found: " + properties.type()));

        argumentType.onArgumentBuilder(this);
        return this;
    }

    public ArgumentProperties build() {

        if (properties.type().isEmpty())
            properties.type("Text");

        var argumentType = argumentTypes
                .findByName(properties.type())
                .orElseThrow(() -> new RuntimeException("Type not found: " + properties.type()));

        if (properties.displayAttributes().isEmpty())
            withDisplayAttribute(DisplayAttribute.NAME, DisplayAttribute.TYPE, DisplayAttribute.ERROR);

        if (properties.defaultValue() == null)
            withDefaultValue(argumentType.defaultValue());

        if (properties.suggestion() == null)
            withSuggestions(argumentType);

        if (properties.parser() == null)
            withParser(argumentType);

        if (properties.suggestion() == null)
            withSuggestions(event -> ActionResult.success(Collections.emptyList()));
        return properties;
    }
}
