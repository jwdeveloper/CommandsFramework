package io.github.jwdeveloper.commands.api;

import io.github.jwdeveloper.commands.api.builders.CommandBuilder;


/**
 * This class is made to decorates commandBuilder with templates
 * The template is an object of any type.
 * This type should have annotations such as
 * <p>
 * #@FCommand
 * #@FArgument that are detected and used for CommandBuilder decoration
 */
public interface TemplateParser {
    /**
     * @param template is a class with annotations, that should be converted into CommandBuilder
     * @param builder  the builder that will be decorated
     * @return the input builder decorated by template object
     */
    CommandBuilder templateToBuilder(Object template, CommandBuilder builder);
}
