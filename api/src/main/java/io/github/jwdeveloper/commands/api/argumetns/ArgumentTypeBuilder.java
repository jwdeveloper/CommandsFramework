package io.github.jwdeveloper.commands.api.argumetns;

import io.github.jwdeveloper.commands.api.functions.ArgumentParser;
import io.github.jwdeveloper.commands.api.functions.ArgumentSuggestions;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;
import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface for building and customizing argument types for command parsing and suggestion.
 * It allows setting up custom actions for argument parsing and suggestions,
 * as well as configuring caching and registering argument types.
 */
public interface ArgumentTypeBuilder {

    /**
     * Enables caching for the argument type, optimizing performance by reusing previously computed results.
     *
     * @return the current instance of {@link ArgumentTypeBuilder}
     */
    ArgumentTypeBuilder cache();

    /**
     * Sets up the action for providing argument suggestions, triggered when the command suggests available options to the user.
     *
     * @param suggestions a lambda method that is invoked when an argument suggestion event occurs
     * @return the current instance of {@link ArgumentTypeBuilder}
     */
    ArgumentTypeBuilder onSuggestionAction(ArgumentSuggestions suggestions);

    /**
     * Sets up the action for parsing arguments, triggered when an argument is being parsed.
     *
     * @param parser the parser function to handle the argument parsing event
     * @return the current instance of {@link ArgumentTypeBuilder}
     */
    ArgumentTypeBuilder onParseAction(ArgumentParser parser);


    /**
     * It is called before an argument is built. It sets default
     * properties for argument
     *
     * @param action action
     * @return the current instance of {@link ArgumentTypeBuilder}
     */
    ArgumentTypeBuilder onArgumentBuilder(Consumer<ArgumentBuilder> action);


    /**
     * Registers the current argument type, or overrides an existing one based on its name.
     *
     * @return the registered {@link ArgumentType}
     */
    ArgumentType register();

    /**
     * Sets up the action for providing argument suggestions using a simplified function interface.
     *
     * @param parser a function that takes an {@link ArgumentSuggestionEvent} and returns a list of suggestion strings
     * @return the current instance of {@link ArgumentTypeBuilder}
     */
    default ArgumentTypeBuilder onSuggestion(Function<ArgumentSuggestionEvent, List<String>> parser) {
        return onSuggestionAction(event -> {
            try {
                return ActionResult.success(parser.apply(event));
            } catch (Exception e) {
                return ActionResult.failed(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for parsing arguments using a simplified function interface.
     *
     * @param parser a function that takes an {@link ArgumentParseEvent} and returns the parsed object
     * @return the current instance of {@link ArgumentTypeBuilder}
     */
    default ArgumentTypeBuilder onParse(Function<ArgumentParseEvent, Object> parser) {
        return onParseAction(event -> {
            try {
                return ActionResult.success(parser.apply(event));
            } catch (Exception e) {
                return ActionResult.failed(e.getMessage());
            }
        });
    }
}