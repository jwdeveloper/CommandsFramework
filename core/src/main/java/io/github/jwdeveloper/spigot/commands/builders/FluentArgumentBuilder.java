package io.github.jwdeveloper.spigot.commands.builders;

import io.github.jwdeveloper.spigot.commands.argumetns.ArgumentTypes;
import io.github.jwdeveloper.spigot.commands.builder.arguments.ArgumentBuilder;
import io.github.jwdeveloper.spigot.commands.argumetns.ArgumentProperties;
import io.github.jwdeveloper.spigot.commands.argumetns.ArgumentType;
import io.github.jwdeveloper.spigot.commands.data.ActionResult;
import io.github.jwdeveloper.spigot.commands.data.DisplayAttribute;
import io.github.jwdeveloper.spigot.commands.functions.ArgumentParser;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

public class FluentArgumentBuilder implements ArgumentBuilder {

    @Getter
    private final ArgumentProperties properties;
    private final Set<ArgumentType> argumentType;
    private final ArgumentTypes argumentTypes;

    public FluentArgumentBuilder(ArgumentProperties properties,
                                 ArgumentTypes argumentTypesRegistry) {
        this.properties = properties;
        argumentType = new TreeSet<>();
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

    public ArgumentProperties build() {
        argumentType.forEach(argumentTypes::register);
        var argumentTypeName = properties.type();
        if (argumentTypeName.isEmpty()) {
            argumentTypeName = "Text";
        }

        var argumentType = argumentTypes
                .findByName(argumentTypeName)
                .orElseThrow(() -> new RuntimeException("Type not found: " + properties.type()));


        if (properties.displayAttributes().isEmpty()) {
            withDisplayAttribute(DisplayAttribute.NAME, DisplayAttribute.TYPE, DisplayAttribute.ERROR);
        }

        withParser(argumentType);
        if (properties.defaultValue() == null)
            withDefaultValue(argumentType.defaultValue());

        if (properties.suggestion() == null)
            withSuggestions(argumentType);

        if (properties.suggestion() == null)
            withSuggestions(event -> ActionResult.success(Collections.emptyList()));

        return properties;
    }
}
