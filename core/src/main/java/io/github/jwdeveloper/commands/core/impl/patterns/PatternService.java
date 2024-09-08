package io.github.jwdeveloper.commands.core.impl.patterns;

import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.DisplayAttribute;
import io.github.jwdeveloper.commands.api.patterns.PatternsRegistry;

public class PatternService {

    private final PatternParser parser;
    private final PatternsRegistry patterns;

    public PatternService(PatternParser patternExpressionService, PatternsRegistry patterns) {
        this.parser = patternExpressionService;
        this.patterns = patterns;
    }

    public ActionResult<CommandBuilder> getCommandBuilder(Object source, String pattern, CommandBuilder builder) {
        var result = parser.resolve(pattern);
        if (result.isFailed()) {
            return result.cast();
        }
        var commandData = result.getValue();
        var namesChain = commandData.namesChain();
        if (!namesChain.isEmpty()) {
            var isFirst = true;
            for (var name : namesChain) {
                if (isFirst) {
                    continue;
                }
                builder = builder.addSubCommand(name);
                isFirst = false;
            }
        }
        builder.properties().name(commandData.name());
        for (var argument : commandData.arguments()) {
            var argumentBuilder = builder.argument(argument.name());

            argumentBuilder
                    .withDefaultValue(argument.defaultValue())
                    .withRequired(argument.required())
                    .withType(argument.type());

            if (!argument.suggestions().isEmpty()) {
                argumentBuilder.withDisplayAttribute(DisplayAttribute.SUGGESTIONS);
                argumentBuilder.withSuggestions(argument.suggestions());
            }

            for (var property : argument.properties()) {
                var key = property.getKey();
                var value = property.getValue();

                patterns.applyMapping(source, key, value, argumentBuilder);
            }
        }
        return ActionResult.success(builder);
    }
}
