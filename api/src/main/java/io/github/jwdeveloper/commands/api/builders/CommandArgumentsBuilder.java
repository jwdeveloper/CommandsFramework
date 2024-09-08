package io.github.jwdeveloper.commands.api.builders;

import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;

import java.util.function.Consumer;

/**
 * Interface for building command arguments.
 *
 * @param <T> The type of the builder implementing this interface.
 */
public interface CommandArgumentsBuilder<T extends CommandBuilder> {

    /**
     * Provides direct access to the builder of a specified argument.
     * If the argument does not exist, it creates a new builder and returns it.
     *
     * @param name the name of the argument.
     * @return the builder for the specified argument.
     */
    ArgumentBuilder argument(String name);

    /**
     * Creates a new argument and provides a consumer with the builder for the specified argument.
     *
     * @param name    the name of the argument.
     * @param builder the consumer that builds the argument.
     * @return the builder instance.
     */
    T addArgument(String name, Consumer<ArgumentBuilder> builder);

    /**
     * Adds a new text argument with a default type of "Text".
     *
     * @param name the name of the argument.
     * @return the builder instance.
     */
    default T addArgument(String name) {
        return addArgument(name, "Text", (x) -> {
        });
    }

    /**
     * Adds a new argument with the specified type.
     *
     * @param name         the name of the argument.
     * @param argumentType the type of the argument.
     * @return the builder instance.
     */
    default T addArgument(String name, String argumentType) {
        return addArgument(name, argumentType, (x) -> {
        });
    }

    /**
     * Adds a new argument with the specified type and a consumer for additional configuration.
     *
     * @param name         the name of the argument.
     * @param argumentType the type of the argument.
     * @param action       the consumer for additional configuration.
     * @return the builder instance.
     */
    default T addArgument(String name, String argumentType, Consumer<ArgumentBuilder> action) {
        return addArgument(name, argumentBuilder -> {
            argumentBuilder.withType(argumentType);
            action.accept(argumentBuilder);
        });
    }

    /**
     * Adds a new number argument.
     * Number argument holds the Double value
     *
     * @param name the name of the argument.
     * @return the builder instance.
     */
    default T addNumberArgument(String name) {
        return addArgument(name, "Number");
    }

    /**
     * Adds a new number argument with additional configuration.
     * Number argument holds the Double value
     *
     * @param name the name of the argument.
     * @param action       the consumer for additional configuration.
     * @return the builder instance.
     */
    default T addNumberArgument(String name, Consumer<ArgumentBuilder> action) {
        return addArgument(name, "Number", action);
    }

    /**
     * Adds a new text argument.
     *
     * @param name the name of the argument.
     * @return the builder instance.
     */
    default T addTextArgument(String name) {
        return addArgument(name, "Text");
    }

    /**
     * Adds a new text argument with additional configuration.
     *
     * @param name the name of the argument.
     * @param action       the consumer for additional configuration.
     * @return the builder instance.
     */
    default T addTextArgument(String name, Consumer<ArgumentBuilder> action) {
        return addArgument(name, "Text", action);
    }


    /**
     * Adds a new boolean argument.
     *
     * @param name the name of the argument.
     * @return the builder instance.
     */
    default T addBoolArgument(String name) {
        return addArgument(name, "Bool");
    }

    /**
     * Adds a new boolean argument with additional configuration.
     *
     * @param name the name of the argument.
     * @param action       the consumer for additional configuration.
     * @return the builder instance.
     */
    default T addBoolArgument(String name, Consumer<ArgumentBuilder> action) {
        return addArgument(name, "Bool", action);
    }

    /**
     * Adds a new player argument.
     *
     * @param name the name of the argument.
     * @return the builder instance.
     */
    default T addPlayerArgument(String name) {
        return addArgument(name, "Player");
    }

    /**
     * Adds a new player argument with additional configuration.
     *
     * @param name the name of the argument.
     * @param action       the consumer for additional configuration.
     * @return the builder instance.
     */
    default T addPlayerArgument(String name, Consumer<ArgumentBuilder> action) {
        return addArgument(name, "Player", action);
    }

    /**
     * Adds a new location argument with a specified name.
     *
     * @param name the name of the argument.
     * @return the builder instance.
     */
    default T addLocationArgument(String name) {
        return addArgument(name, "Location");
    }


    /**
     * Adds a new entity argument using the EntityType enum.
     *
     * @return the builder instance.
     */
    default T addEntityArgument(String name) {
        return addArgument(name, "Entity");
    }


    /**
     * Adds a new sound argument using the Sound enum.
     *
     * @return the builder instance.
     */
    default T addSoundArgument(String name) {
        return addArgument(name, "Sound");
    }


    /**
     * Adds a new color argument using the ChatColor enum.
     *
     * @return the builder instance.
     */
    default T addColorArgument(String name) {
        return addArgument(name, "Color");
    }


    /**
     * Adds a new particle argument using the Particle enum.
     *
     * @return the builder instance.
     */
    default T addParticleArgument(String name) {
        return addArgument(name, "Particle");
    }


    /**
     * Adds a new particle argument using the Material enum.
     *
     * @return the builder instance.
     */
    default T addMaterialArgument(String name) {
        return addArgument(name, "Material");
    }
}