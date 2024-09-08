package io.github.jwdeveloper.commands.api.data;


/**
 * Defines all the possible sender types
 * Sender is an object that invoke a command
 */
public enum SenderType {
    /**
     * The player
     */
    PLAYER(),
    /**
     * The CONSOLE
     */
    CONSOLE(),
    /**
     * The PROXY
     */
    PROXY(),
    /**
     * The BLOCK
     */
    BLOCK(),
    /**
     * The REMOTE_CONSOLE
     */
    REMOTE_CONSOLE();
}
