package io.github.jwdeveloper.commands.core.impl;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentType;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentTypeBuilder;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentsTypesRegistry;
import io.github.jwdeveloper.commands.api.data.DisplayAttribute;
import io.github.jwdeveloper.commands.core.impl.builders.ArgumentTypeBuilderImpl;

import java.util.*;

public class DefaultArgumentTypesRegistry implements ArgumentsTypesRegistry {

    private final Map<String, ArgumentType> parsers = new HashMap<>();

    @Override
    public List<ArgumentType> findAll() {
        return parsers.values().stream().toList();
    }

    @Override
    public Optional<ArgumentType> findByName(String name) {
        return Optional.ofNullable(parsers.get(name.toLowerCase()));
    }

    @Override
    public ArgumentTypeBuilder create(String name) {
        return new ArgumentTypeBuilderImpl(name, this);
    }

    @Override
    public ArgumentTypeBuilder createEnum(String name, Class<? extends Enum<?>> enumType) {
        Class enumClazz = enumType;
        var enumValues = Arrays.stream(enumType.getEnumConstants()).map(Enum::name).toList();
        var builder = create(name);
        builder.onParse(event ->
        {
            var value = event.nextArgument();
            return Enum.valueOf(enumClazz, value);
        });
        builder.onSuggestion(argumentSuggestionEvent ->
        {
            var value = argumentSuggestionEvent.rawValue();
            return enumValues.stream().filter(e -> e.toLowerCase().contains(value.toLowerCase())).toList();
        });
        builder.onArgumentBuilder(builder1 ->
        {
            builder1.withDisplayAttribute(DisplayAttribute.SUGGESTIONS);
        });
        return builder;
    }

    @Override
    public ArgumentTypeBuilder createEnum(Class<? extends Enum<?>> enumType) {
        return createEnum(enumType.getSimpleName(), enumType);
    }

    @Override
    public void register(ArgumentType argumentType) {
        parsers.put(argumentType.name().toLowerCase(), argumentType);
    }

    @Override
    public void unregister(ArgumentType parser) {
        parsers.remove(parser.name().toLowerCase());
    }
}
