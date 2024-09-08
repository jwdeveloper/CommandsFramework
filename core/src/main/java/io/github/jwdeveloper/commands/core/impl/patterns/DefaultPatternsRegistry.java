package io.github.jwdeveloper.commands.core.impl.patterns;

import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.patterns.PatterMapper;
import io.github.jwdeveloper.commands.api.patterns.PatternsRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultPatternsRegistry implements PatternsRegistry {


    private final Map<String, PatterMapper> argumentMappers = new HashMap<>();
    private final Map<String, PatterMapper> commandMappers = new HashMap<>();

    @Override
    public PatternsRegistry registerForArgument(String symbol, PatterMapper<ArgumentBuilder> mapper) {
        argumentMappers.put(symbol, mapper);
        return this;
    }


    @Override
    public PatternsRegistry registerForCommand(String symbol, PatterMapper<CommandBuilder> mapper) {
        commandMappers.put(symbol, mapper);
        return this;
    }

    @Override
    public boolean apply(Object source, String symbolKey, String symbolValue, Object builder) {

        Map<String, PatterMapper> map = null;
        if (builder instanceof ArgumentBuilder) {
            map = argumentMappers;
        }
        if (builder instanceof CommandBuilder) {
            map = commandMappers;
        }

        if (!map.containsKey(symbolKey)) {
            return false;
        }
        var mapper = map.get(symbolKey);

        try {
            mapper.map(symbolValue, builder, source);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
