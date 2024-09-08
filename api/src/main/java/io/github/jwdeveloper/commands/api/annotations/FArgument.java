package io.github.jwdeveloper.commands.api.annotations;

import io.github.jwdeveloper.commands.api.data.DisplayAttribute;

import java.lang.annotation.*;


/**
 * Annotation to define argument
 * This annotation can be applied to classes or methods to provide command arguments information
 * such as name, description, usage, and behavior settings.
 */
@Repeatable(FArguments.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface FArgument {
    String name() default "";

    String type() default "Text";

    String description() default "";

    String defaultValue() default "";

    boolean allowNullOutput() default false;

    int index() default -1;

    DisplayAttribute[] displayAttributes() default {DisplayAttribute.NAME, DisplayAttribute.TYPE, DisplayAttribute.ERROR};


    String onParse() default "";

    String onSuggestions() default "";

}
