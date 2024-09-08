package io.github.jwdeveloper.commands.api.patterns;

import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;

/**
 * Register the patterns symbols that can be used during the
 * Command pattern declaration
 * <p>
 * For example symbol will be `amount` in the command below
 * <p>
 * /hello <arg(amount=123)>
 */
public interface PatternsRegistry {


    /**
     * Register single symbol and action that is invoked
     * when symbol is detected during the command creating
     *
     * @param symbol the symbol identifier
     * @param mapper the matter action, invoked during action
     * @return self instance
     */
    PatternsRegistry registerForArgument(String symbol, PatterMapper<ArgumentBuilder> mapper);


    /**
     * Register single symbol and action that is invoked
     * when symbol is detected during the command creating
     *
     * @param symbol the symbol identifier
     * @param mapper the matter action, invoked during action
     * @return self instance
     */
    PatternsRegistry registerForCommand(String symbol, PatterMapper<CommandBuilder> mapper);


    boolean apply(Object source, String key, String value, Object builder);
}
