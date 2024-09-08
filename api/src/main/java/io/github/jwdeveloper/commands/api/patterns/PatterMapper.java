package io.github.jwdeveloper.commands.api.patterns;

import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;

public interface PatterMapper<T> {

    void map(String value, T builder, Object source) throws Exception;
}
