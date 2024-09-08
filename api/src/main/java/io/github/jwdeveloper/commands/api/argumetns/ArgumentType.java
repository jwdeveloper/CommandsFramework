package io.github.jwdeveloper.commands.api.argumetns;

import io.github.jwdeveloper.commands.api.functions.ArgumentParser;
import io.github.jwdeveloper.commands.api.functions.ArgumentSuggestions;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;
import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;

import java.util.Collections;
import java.util.List;


/**
 * ArgumentType is a class with predefined rules of how
 * certain argument should be parsed.
 * <p>
 * Additionally, how can define suggestions with it
 */
public interface ArgumentType extends ArgumentParser, ArgumentSuggestions {


    /**
     * It is called before an argument is built. It sets default
     * properties for argument
     *
     * @param builder action
     */
    void onArgumentBuilder(ArgumentBuilder builder);


    /**
     * @return the name identifier
     */
    String name();

    /**
     * @return the default value of argument, when parameter has not been providing during command execution
     */
    default Object defaultValue() {
        return "";
    }

    /**
     * Triggered when argument values are being suggested to sender
     *
     * @param event an events that holds information about current command parameters
     * @return ActionResult of List the result can be success or failed
     */
    default ActionResult<List<String>> onSuggestion(ArgumentSuggestionEvent event) {
        return ActionResult.success(Collections.emptyList());
    }
}
