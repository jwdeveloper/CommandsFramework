package io.github.jwdeveloper.commands.api.builders;

import io.github.jwdeveloper.commands.api.data.CommandProperties;
import io.github.jwdeveloper.commands.api.data.SenderType;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Builder sets the command properties
 *
 * @param <BUILDER> builder type
 */
public interface CommandPropsBuilder<BUILDER> {

    /**
     * @return the properties object to direct modification
     */
    CommandProperties properties();

    /**
     * @param properties action that modifies the command properties object
     * @return self
     */
    BUILDER withProperties(Consumer<CommandProperties> properties);


    /**
     * Sets the aliases
     *
     * @param aliases the command description
     * @return builder
     */
    default BUILDER withAliases(String... aliases) {
        return withProperties(commandProperties ->
        {
            properties().aliases(aliases);
        });
    }

    /**
     * Sets the command label
     *
     * @param label the command description
     * @return builder
     */
    default BUILDER withLabel(String label) {
        return withProperties(commandProperties ->
        {
            properties().label(label);
        });
    }


    /**
     * Use message best for the command documentation purpose
     *
     * @param usageMessage the command description
     * @return builder
     */
    default BUILDER withUsageMessage(String usageMessage) {
        return withProperties(commandProperties ->
        {
            properties().usageMessage(usageMessage);
        });
    }

    /**
     * Short description best for the command documentation purpose
     *
     * @param shortDescription the command description
     * @return builder
     */
    default BUILDER withShortDescription(String shortDescription) {
        return withProperties(commandProperties ->
        {
            properties().shortDescription(shortDescription);
        });
    }

    /**
     * Description best for the command documentation purpose
     *
     * @param description the command description
     * @return builder
     */
    default BUILDER withDescription(String description) {
        return withProperties(commandProperties ->
        {
            properties().description(description);
        });
    }

    /**
     * Assign the permission to command.
     * Permission is checked before command validation event
     *
     * @param permissions the permission value
     * @return builder
     */
    default BUILDER withPermission(String permissions) {
        return withProperties(commandProperties ->
        {
            commandProperties.permission(permissions);
        });
    }

    /**
     * Make command hide from the commands list
     *
     * @param isHide is command hide
     * @return builder
     */
    default BUILDER withHideFromSuggestions(boolean isHide) {
        return withProperties(commandProperties ->
        {
            commandProperties.hideFromSuggestions(isHide);
        });
    }

    /**
     * Disable/Enable command from being active.
     * Disabled command will not be visible in the Hints and Command list
     *
     * @param isActive the command active state
     * @return builder
     */
    default BUILDER withIsActive(boolean isActive) {
        return withProperties(commandProperties ->
        {
            commandProperties.active(isActive);
        });
    }

    /**
     * Set the sender types that are disabled for command
     *
     * @param senderTypes one or more sender type
     * @return builder
     */
    default BUILDER withExcludedSenders(SenderType... senderTypes) {
        return withProperties(commandProperties ->
        {
            properties().excludedSenders().addAll(Arrays.stream(senderTypes).toList());
        });
    }


    /**
     * Sets the custom property name and value
     *
     * @param name  the property name
     * @param value the property value
     * @return builder
     */
    default BUILDER withProperty(String name, Object value) {
        return withProperties(commandProperties ->
        {
            properties().customProperties().put(name, value);
        });
    }

}
