package io.github.jwdeveloper.commands.api.argumetns;

import io.github.jwdeveloper.commands.api.functions.ArgumentParser;
import io.github.jwdeveloper.commands.api.functions.ArgumentSuggestions;
import io.github.jwdeveloper.commands.api.data.DisplayAttribute;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(fluent = true)
public class ArgumentProperties {
    /**
     * The index of the parameter in the argument list. Parameters are sorted in decreasing order of this index.
     */
    private int index = -1;

    /**
     * The name of the argument, which is displayed when using the DisplayAttribute.NAME attribute.
     */
    private String name = "";

    /**
     * The description of the argument, which is displayed when using the DisplayAttribute.DESCRIPTION attribute.
     */
    private String description = "";

    /**
     * The type of the argument's value. This information is displayed when using the DisplayAttribute.TYPE attribute.
     */
    private String type = "";

    /**
     * The default value of the argument, used when no value is provided for this parameter in the input commands.
     */
    private String defaultValue = "";

    /**
     * Indicates whether this parameter is required. If true, the parameter must be provided in the input commands.
     */
    private boolean required = true;

    /**
     * Specifies whether the ArgumentParser allows the output value to be null.
     */
    private boolean allowNullOutput = false;

    /**
     * A functional interface that is triggered when the parameter is being parsed.
     */
    private ArgumentParser parser;

    /**
     * Provides suggestions for the argument values.
     */
    private ArgumentSuggestions suggestion;

    /**
     * A set of display attributes that control how the command is displayed in the chat.
     * You can add or remove display attributes as needed.
     */
    private Set<DisplayAttribute> displayAttributes = new HashSet<>();

    /**
     * Custom properties associated with the command, which may include additional permissions, statistics, or other metadata.
     */
    private Map<String, Object> properties = new HashMap<>();

    /**
     * Checks if a specific display attribute is present in the set of display attributes.
     *
     * @param attribute The display attribute to check for.
     * @return true if the attribute is present, false otherwise.
     */
    public boolean hasDisplayAttribute(DisplayAttribute attribute) {
        return displayAttributes.contains(attribute);
    }
}