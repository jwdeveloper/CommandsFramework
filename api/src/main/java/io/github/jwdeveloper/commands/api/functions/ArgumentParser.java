package io.github.jwdeveloper.commands.api.functions;

import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;

/**
 *  The function interface used to be triggered
 *  When a command is getting parsed
 */
@FunctionalInterface
public interface ArgumentParser {
    ActionResult<Object> onParse(ArgumentParseEvent event);
}
