package io.github.jwdeveloper.commands.core.impl.patterns;

import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.patterns.PatterMapper;
import io.github.jwdeveloper.commands.api.patterns.PatternsRegistry;

import java.util.HashMap;
import java.util.Map;

public class PatternsImpl implements PatternsRegistry {

    private final Map<String, PatterMapper> propertyMappers = new HashMap<>();
    private final Map<String, PatterMapper> symbolsMappers = new HashMap<>();

    @Override
    public PatternsRegistry mapProperty(String property, PatterMapper mapper) {
        propertyMappers.put(property, mapper);
        return this;
    }


    @Override
    public boolean applyMapping(Object source, String key, String value, ArgumentBuilder argumentBuilder) {
        if (!propertyMappers.containsKey(key)) {
            return false;
        }
        var mapper = propertyMappers.get(key);
        try {
            mapper.map(value, argumentBuilder, source);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
