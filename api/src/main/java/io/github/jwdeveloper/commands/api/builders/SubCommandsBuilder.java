package io.github.jwdeveloper.commands.api.builders;

import java.util.function.Consumer;


/**
 * The class made for adding sub-command (children) of the
 * command
 *
 * @param <BUILDER> the builder type
 */
public interface SubCommandsBuilder<BUILDER extends CommandBuilder<BUILDER, ?>> {

    /**
     * Creates a builder of child command
     *
     * @param pattern the child command pattern
     * @return the command build of child
     */
    BUILDER addSubCommand(String pattern);

    /**
     * Creates a builder of child command
     *
     * @param pattern       the child command pattern
     * @param builderAction the action with child command builder as an input
     * @return the builder instance
     */
    BUILDER addSubCommand(String pattern, Consumer<BUILDER> builderAction);

}
