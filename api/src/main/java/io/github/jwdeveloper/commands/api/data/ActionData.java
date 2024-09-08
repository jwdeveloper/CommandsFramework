package io.github.jwdeveloper.commands.api.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Data
@Accessors(fluent = true)
public class ActionData {
    private String tag;
    private Class<?> inputType;
    private Function function;
}