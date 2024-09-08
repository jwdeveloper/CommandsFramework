package io.github.jwdeveloper.commands.api.functions;

import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;

import java.util.List;

/**
 * The functional interfaced used when command or argument suggestion
 * are being parsed
 */
@FunctionalInterface
public interface ArgumentSuggestions {
    ActionResult<List<String>> onSuggestion(ArgumentSuggestionEvent event);
}
