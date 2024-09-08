package io.github.jwdeveloper.commands.api.builders.arguments;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import io.github.jwdeveloper.commands.api.functions.ArgumentParser;
import io.github.jwdeveloper.commands.api.functions.ArgumentSuggestions;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.DisplayAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Interface for building and configuring arguments for command parsing.
 * Provides methods to set various properties, such as name, type, suggestions,
 * and display attributes, as well as custom parsers and validation rules.
 */
public interface ArgumentBuilder {

    /**
     * Allows configuration of argument properties using a consumer function.
     *
     * @param action a consumer function that configures the {@link ArgumentProperties}
     * @return the current instance of {@link ArgumentBuilder}
     */
    ArgumentBuilder withProperty(Consumer<ArgumentProperties> action);

    /**
     * Sets the index of the argument in the command's argument list.
     *
     * @param index the position of the argument
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withIndex(int index) {
        return withProperty(e -> e.index(index));
    }

    /**
     * Sets the description for the argument, which is often displayed in help or suggestions.
     *
     * @param description the description text of the argument
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDescription(String description) {
        return withProperty(e -> e.description(description));
    }

    /**
     * Sets the name of the argument, which is used for identification in the command.
     *
     * @param name the name of the argument
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withName(String name) {
        return withProperty(e -> e.name(name));
    }

    /**
     * Sets the type of the argument, defining what kind of data the argument should hold.
     *
     * @param argumentType the type identifier of the argument
     * @return the current instance of {@link ArgumentBuilder}
     */
    ArgumentBuilder withType(String argumentType);

    /**
     * Sets whether the argument is required or optional.
     *
     * @param required true if the argument is required, false otherwise
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withRequired(boolean required) {
        return withProperty(e -> e.required(required));
    }

    /**
     * Sets display attributes for the argument, which control what information
     * is shown to the user when the command is used.
     *
     * @param displayMode one or more display attributes to be applied
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDisplayAttribute(DisplayAttribute... displayMode) {
        return withProperty(e -> e.displayAttributes().addAll(Arrays.stream(displayMode).toList()));
    }

    /**
     * Sets the argument to display its description when shown to the user.
     *
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDisplayDescription() {
        return withDisplayAttribute(DisplayAttribute.DESCRIPTION);
    }

    /**
     * Sets the argument to display its name when shown to the user.
     *
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDisplayName() {
        return withDisplayAttribute(DisplayAttribute.NAME);
    }

    /**
     * Sets the argument to display its type when shown to the user.
     *
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDisplayType() {
        return withDisplayAttribute(DisplayAttribute.TYPE);
    }

    /**
     * Sets the argument to not display any information to the user.
     *
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDisplayNone() {
        return withDisplayAttribute(DisplayAttribute.NONE);
    }

    /**
     * Sets the argument to display error messages when there is an issue with input.
     *
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDisplayError() {
        return withDisplayAttribute(DisplayAttribute.ERROR);
    }

    /**
     * Sets the argument to display suggestions when the user is typing the command.
     *
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDisplaySuggestions() {
        return withDisplayAttribute(DisplayAttribute.SUGGESTIONS);
    }

    /**
     * Configures the argument to provide custom suggestions to the user.
     *
     * @param suggestions a function that generates suggestions based on the user input
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withSuggestions(ArgumentSuggestions suggestions) {
        return withProperty(e -> e.suggestion(suggestions));
    }

    /**
     * Configures the argument to provide a predefined list of suggestions to the user.
     *
     * @param suggestions an array of suggestion strings
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withSuggestions(String... suggestions) {
        return withSuggestions((e) -> ActionResult.success(List.of(suggestions)));
    }

    /**
     * Configures the argument to provide a predefined list of suggestions to the user.
     *
     * @param suggestions a list of suggestion strings
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withSuggestions(List<String> suggestions) {
        return withSuggestions((e) -> ActionResult.success(suggestions));
    }

    /**
     * Sets whether the argument allows default output when no specific value is provided.
     *
     * @param value true to allow default output, false otherwise
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withAllowDefaultOutput(boolean value) {
        return withProperty(e -> e.allowNullOutput(value));
    }

    /**
     * Sets a custom parser for the argument, which handles converting input into the required format.
     *
     * @param parser the custom parser function for the argument
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withParser(ArgumentParser parser) {
        return withProperty(e -> e.parser(parser));
    }

    /**
     * Sets the default value for the argument if no value is provided by the user.
     *
     * @param defaultValue the default value to use
     * @return the current instance of {@link ArgumentBuilder}
     */
    default ArgumentBuilder withDefaultValue(Object defaultValue) {
        return withProperty(e -> e.defaultValue(defaultValue.toString()));
    }

}