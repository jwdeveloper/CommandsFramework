package io.github.jwdeveloper.commands.api.patterns;

import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;

public interface PatterMapper {

    void map(String value, ArgumentBuilder builder, Object source) throws Exception;
}
