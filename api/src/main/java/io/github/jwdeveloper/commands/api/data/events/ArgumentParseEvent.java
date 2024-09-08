package io.github.jwdeveloper.commands.api.data.events;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import io.github.jwdeveloper.commands.api.iterators.ArgumentIterator;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Payload of the argument parse event
 */
@Data
@Accessors(fluent = true)
public class ArgumentParseEvent<SENDER> {

    /**
     * The command
     */
    private Command command;
    /**
     * The argument properties
     */
    private ArgumentProperties argument;

    /**
     * The object that invoked the command
     */
    private SENDER sender;
    /**
     * The Argument iterator, used for iterating over the provided
     * argument values in input
     */
    private ArgumentIterator iterator;


    /**
     * @return true if there is next argument available
     */
    public boolean hasNextArgument() {
        return iterator.hasNext();
    }

    /**
     * @return the next argument value
     */
    public String nextArgument() {
        return iterator.next();
    }

    /**
     * @return the current argument value
     */
    public String currentArgument() {
        return iterator.current();
    }
}
