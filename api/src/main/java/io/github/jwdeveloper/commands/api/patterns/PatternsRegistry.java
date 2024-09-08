package io.github.jwdeveloper.commands.api.patterns;

import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;

public interface PatternsRegistry {
    PatternsRegistry mapProperty(String property, PatterMapper mapper);
    boolean applyMapping(Object source, String key, String value, ArgumentBuilder argumentBuilder);
}
