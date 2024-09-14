package io.github.jwdeveloper.commands.api.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class holds all the information about a command
 */
@Data
@Accessors(fluent = true)
public class CommandProperties {

    /**
     * The command name
     */
    private String name;

    /**
     * Contains list of Sender types can
     * CAN NOT use this command
     */
    private List<Class<?>> excludedSenders = new ArrayList<>();

    /**
     * Command short description
     */
    private String shortDescription = "";

    /**
     * Command description
     */
    private String description = "";

    /**
     * Command usage message
     */
    private String usageMessage = "";

    /**
     * Command aliases
     */
    private String[] aliases = new String[0];

    /**
     * Label
     */
    private String label = "";

    /**
     * Set the permission that sender need to have in order to invoke command
     */
    private String permission = "";

    /**
     * Hides the command from suggestion, it will not be displayed
     */
    private boolean hideFromSuggestions = false;


    /**
     * Defines is command is enabled and can be used
     */
    private boolean active = true;

    /**
     * Map of command custom properties,
     * you can add to it tags or additional command data
     */
    private Map<String, Object> customProperties = new HashMap<>();

    /**
     * Get the custom property by name
     *
     * @param propertyName the name of custom property
     * @return custom property value
     */
    public Object get(String propertyName) {
        return customProperties.get(propertyName);
    }


    /**
     * Get the custom property by name
     *
     * @param propertyName the name of custom property
     * @param <T>          the type of value
     * @return custom property value
     */
    public <T> T get(String propertyName, Class<T> returnType) {
        return (T) get(propertyName);
    }
}
