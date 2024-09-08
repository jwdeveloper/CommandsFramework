package io.github.jwdeveloper.commands.api.data.events;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The payload of the argument suggestion event
 */
@Data
@Accessors(fluent = true)
public class ArgumentSuggestionEvent<SENDER> {

    /**
     * The command
     */
    private Command command;
    /**
     * The argument properties
     */
    private ArgumentProperties argument;
    /**
     * The command sender
     */
    private SENDER sender;
    /**
     * The input raw string value
     */
    private String rawValue;
    /**
     * The argument parsed value
     */
    private Object value;
}