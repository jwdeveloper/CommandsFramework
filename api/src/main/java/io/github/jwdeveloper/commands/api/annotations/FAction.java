package io.github.jwdeveloper.commands.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FAction {
    String identifier() default "";
}
