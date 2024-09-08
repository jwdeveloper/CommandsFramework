package io.github.jwdeveloper.commands.api.data;

/**
 * Defines how does an argument should be displayed
 */
public enum DisplayAttribute {
    /**
     * Does not display anything
     */
    NONE,
    /**
     * Display the argument name
     */
    NAME,
    /**
     * Display the argument type
     */
    TYPE,
    /**
     * Display the argument suggestions list
     */
    SUGGESTIONS,
    /**
     * Display the argument description
     */
    DESCRIPTION,
    /**
     * Display error triggered during the argument parsing
     */
    ERROR
}
