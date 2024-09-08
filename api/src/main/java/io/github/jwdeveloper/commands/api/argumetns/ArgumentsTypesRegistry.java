package io.github.jwdeveloper.commands.api.argumetns;

import io.github.jwdeveloper.commands.api.data.DisplayAttribute;

import java.util.List;
import java.util.Optional;


/**
 * Manage and holds ArgumentTypes,{@link ArgumentsTypesRegistry}.
 */
public interface ArgumentsTypesRegistry {

    /**
     * @return List of all registered argument types
     */
    List<ArgumentType> findAll();

    /**
     * @param name argument name
     * @return Optional empty when ArgumentType has not been found
     */
    Optional<ArgumentType> findByName(String name);

    /**
     * You can create new or override existing argument type
     *
     * @param name argument identifier name
     * @return argument type builder for adjust properties
     */
    ArgumentTypeBuilder create(String name);

    /**
     * Use to avoid boilerplate for adding enum arguments
     * By the default Enums argument will have display mode set to
     * DisplayAttribute.Suggestions
     *
     * @param name     argument identifier name
     * @param enumType type of enum
     * @return argument type builder configured to be enum
     * @see DisplayAttribute
     */
    ArgumentTypeBuilder createEnum(String name, Class<? extends Enum<?>> enumType);

    /**
     * Use to avoid boilerplate for adding enum arguments
     * By the default Enums argument will have display mode set to
     * DisplayAttribute.Suggestions
     *
     * @param enumType type of enum
     * @return argument type builder configured to be enum
     */
    ArgumentTypeBuilder createEnum(Class<? extends Enum<?>> enumType);


    /**
     * Registers an argument type and makes it active
     *
     * @param argumentType instance of argument type.
     */
    void register(ArgumentType argumentType);

    /**
     * Unregisters argument type
     *
     * @param argumentType instance of argument type.
     */
    void unregister(ArgumentType argumentType);
}
