package io.github.jwdeveloper.commands.core.impl.templates;

import io.github.jwdeveloper.commands.api.annotations.FCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class TemplateData {
    private Object target;

    private Class<?> targetType;

    private Optional<FCommand> commandAnnotation;

    private Map<Method, FCommand> commandMethods;
}
