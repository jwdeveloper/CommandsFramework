package io.github.jwdeveloper.commands.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define command
 * This annotation can be applied to classes or methods to provide command information
 * such as name, description, usage, permissions, and behavior settings.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface FCommand {

    /**
     * Defines the pattern for matching command inputs. This can be used to specify
     * custom argument structures or command syntax.
     *
     * @return the pattern string
     */
    String pattern() default "";

    /**
     * Specifies the name of the command.
     *
     * @return the name of the command
     */
    String name() default "";

    /**
     * Provides a detailed description of the command's functionality.
     *
     * @return the description of the command
     */
    String description() default "";

    /**
     * Provides a brief summary or short description of the command.
     *
     * @return the short description of the command
     */
    String shortDescription() default "";

    /**
     * Specifies the usage message that explains how to use the command.
     *
     * @return the usage message
     */
    String usageMessage() default "";

    /**
     * Sets the label for the command, often used as the primary identifier for execution.
     *
     * @return the label of the command
     */
    String label() default "";

    /**
     * Defines the permission node required to execute this command.
     *
     * @return the permission required for the command
     */
    String permission() default "";

    /**
     * Lists alternative names or aliases for the command.
     *
     * @return an array of alias names
     */
    String[] aliases() default "";

    /**
     * Determines if the command should be hidden from the list of available commands.
     *
     * @return true if the command should be hidden, false otherwise
     */
    boolean hideFromCommands() default false;

    /**
     * Specifies a custom method or action to be invoked when the command's validation process occurs.
     *
     * @return the name of the validation method or handler
     */
    String onValidation() default "";

    /**
     * Specifies a custom method or action to be invoked when an error occurs during command execution.
     *
     * @return the name of the error handler method
     */
    String onError() default "";

    /**
     * Specifies a custom method or action to be invoked when the command is being built or registered.
     *
     * @return the name of the build handler method
     */
    String onBuild() default "";

    /**
     * Invoked when command is finished
     *
     * @return finished event
     */
    String onFinished() default "";
}