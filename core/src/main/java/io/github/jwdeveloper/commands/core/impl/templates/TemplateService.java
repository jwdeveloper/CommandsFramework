package io.github.jwdeveloper.commands.core.impl.templates;

import io.github.jwdeveloper.dependance.api.DependanceContainer;

import io.github.jwdeveloper.commands.api.CommandsRegistry;
import io.github.jwdeveloper.commands.api.TemplateParser;
import io.github.jwdeveloper.commands.api.annotations.FAction;
import io.github.jwdeveloper.commands.api.annotations.FArgument;
import io.github.jwdeveloper.commands.api.annotations.FCommand;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.services.ActionsRegistry;
import io.github.jwdeveloper.commands.core.impl.patterns.PatternBuilderVisitor;
import io.github.jwdeveloper.commands.core.impl.services.ActionBindingService;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateService implements TemplateParser {

    private final DependanceContainer container;
    private final PatternBuilderVisitor patternService;
    private final CommandsRegistry commandsRegistry;
    private final ActionBindingService bindingService;
    private final ActionsRegistry actionsRegistry;
    private final Map<CommandBuilder, String> invokeBuilder;

    public TemplateService(
            ActionBindingService bindingService,
            PatternBuilderVisitor patternService,
            DependanceContainer container,
            ActionsRegistry actionsRegistry,
            CommandsRegistry commandsRegistry) {
        this.bindingService = bindingService;
        this.container = container;
        this.patternService = patternService;
        this.actionsRegistry = actionsRegistry;
        this.commandsRegistry = commandsRegistry;
        this.invokeBuilder = new HashMap<>();
    }

    @Override
    public CommandBuilder templateToBuilder(Object template, CommandBuilder builder) {
        registerActions(template);

        var model = getModel(template);

        if (model.getCommandAnnotation().isPresent()) {
            decorateBuilder(template, model.getCommandAnnotation().get(), builder);
        } else {
            builder.withProperty("Parent", false);
        }

        for (var entry : model.getCommandMethods().entrySet()) {
            var commandName = getCommandName(entry.getValue().name(), entry.getValue().pattern());
            var targetBuilder = getBuilder(commandName, builder);
            handleCommandMethod(template, entry.getKey(), entry.getValue(), targetBuilder);
        }

        for (var entry : invokeBuilder.entrySet()) {
            handleBuilderMethod(template, entry.getValue(), entry.getKey());
        }

        var command = builder.build();
        for (var child : command.children()) {
            commandsRegistry.add(child);
        }

        return builder;
    }

    private CommandBuilder decorateBuilder(Object template, FCommand fCommand, CommandBuilder builder) {
        var pattern = fCommand.pattern();
        var name = fCommand.name();

        if (!pattern.isEmpty() && !name.isEmpty()) {
            throw new RuntimeException("You need to choose Pattern or Name, they can not be use both");
        }

        if (!pattern.isEmpty()) {
            var patternResult = patternService.getCommandBuilder(template, pattern, builder);
            if (patternResult.isFailed()) {
                throw new RuntimeException(patternResult.getMessage());
            }
            builder = patternResult.getValue();
        }

        if (!fCommand.description().isEmpty())
            builder.withDescription(fCommand.description());

        if (!fCommand.shortDescription().isEmpty())
            builder.withShortDescription(fCommand.shortDescription());

        if (!fCommand.permission().isEmpty())
            builder.withPermission(fCommand.permission());

        if (!fCommand.label().isEmpty())
            builder.withLabel(fCommand.label());

        if (!fCommand.usageMessage().isEmpty())
            builder.withUsageMessage(fCommand.usageMessage());

        builder.withAliases(fCommand.aliases());
        builder.withHideFromSuggestions(fCommand.hideFromCommands());

        if (!fCommand.onFinished().isEmpty())
          //  builder.onFinished(bindingService.bindFinishedMethod(template, fCommand.onFinished()));

        if (!fCommand.onError().isEmpty())
           // builder.onError(bindingService.bindErrorMethod(template, fCommand.onError()));

        if (!fCommand.onValidation().isEmpty())
           // builder.onValidation(bindingService.bindValidationMethod(template, fCommand.onValidation()));

        if (!fCommand.onBuild().isEmpty())
            invokeBuilder.put(builder, fCommand.onBuild());

        return builder;
    }


    private void handleBuilderMethod(Object target, String methodName, CommandBuilder builder) {
        try {
            var method = bindingService.getMethod(target, methodName);
            var methodContainer = container.createChildContainer()
                    .registerSingleton(CommandBuilder.class, builder)
                    .build();
            var params = methodContainer.resolveParameters(method);
            method.invoke(target, params);
        } catch (Exception e) {
            throw new RuntimeException("Error while invoking builder", e);
        }
    }

    private void handleCommandMethod(Object target, Method method, FCommand fCommand, CommandBuilder builder) {
        builder = decorateBuilder(target, fCommand, builder);
//        var senderTypeOptional = Arrays.stream(method.getParameterTypes())
//                .filter(CommandSender.class::isAssignableFrom)
//                .findFirst();
//
//        var senderType = senderTypeOptional.orElse(CommandSender.class);
//        var handler = new TemplateMethodHandler(target, method, container.createChildContainer());
     //   builder.onEvent(senderType, handler::invokeEvent);


        var arguments = target.getClass().getDeclaredAnnotationsByType(FArgument.class);
        for (var argument : arguments) {
            handleCommandArgument(target, argument, builder);
        }
    }

    private void handleCommandArgument(Object target, FArgument fArgument, CommandBuilder builder) {

        var argumentName = fArgument.name();
        if (argumentName.isEmpty()) {
            throw new RuntimeException("Argument name could not be empty");
        }
        var argumentBuilder = builder.argument(argumentName);

        if (!fArgument.type().isEmpty())
            argumentBuilder.withType(fArgument.type());

        if (!fArgument.description().isEmpty())
            argumentBuilder.withDescription(fArgument.description());

        if (!fArgument.defaultValue().isEmpty())
            argumentBuilder.withDefaultValue(fArgument.defaultValue());

        if (fArgument.index() > -1)
            argumentBuilder.withIndex(fArgument.index());

        argumentBuilder.withAllowDefaultOutput(fArgument.allowNullOutput());
        argumentBuilder.withDisplayAttribute(fArgument.displayAttributes());

        if (!fArgument.onParse().isEmpty()) {
            argumentBuilder.withParser(bindingService.bindParseMethod(target, fArgument.onParse()));
        }

        if (!fArgument.onSuggestions().isEmpty()) {
            argumentBuilder.withSuggestions(bindingService.bindSuggestionsMethod(target, fArgument.onSuggestions()));
        }
    }

    private TemplateData getModel(Object template) {
        var commandAnnotation = getAnnotation(template.getClass(), FCommand.class);
        var commandsMap = getAllMethod(template.getClass())
                .stream()
                .filter(e -> e.isAnnotationPresent(FCommand.class))
                .filter(e ->
                {
                    e.setAccessible(true);
                    return true;
                })
                .collect(Collectors.toMap(e -> e, e -> e.getAnnotation(FCommand.class)));

        var annotation = Optional.ofNullable(commandAnnotation.getValue());
        return new TemplateData(
                template,
                template.getClass(),
                annotation,
                commandsMap);
    }


    public void registerActions(Object source) {
        var methods = getAllMethod(source.getClass())
                .stream()
                .filter(e -> e.isAnnotationPresent(FAction.class))
                .toList();

        for (var method : methods) {
            var annotation = method.getAnnotation(FAction.class);
            var name = annotation.identifier().isEmpty() ? method.getName() : annotation.identifier();

        }
    }


    /**
     * I know at the first glance it looks useless
     * <p>
     * This method is made since I need both private and public methods,
     * however the `type.getDeclaredMethods()` does not return methods from the
     * super class. That is way I need to combine both methods
     **/
    private Set<Method> getAllMethod(Class<?> type) {
        var methods = Arrays.stream(type.getMethods()).toList();
        var delcared = Arrays.stream(type.getDeclaredMethods()).toList();

        var set = new HashSet<Method>();
        set.addAll(methods);
        set.addAll(delcared);
        return set;
    }


    private String getCommandName(String name, String pattern) {
        if (name.isEmpty() && pattern.isEmpty()) {
            return "";
        }

        if (!name.isEmpty()) {
            if (name.startsWith("/")) {
                name = name.replace("/", "");
            }
            return name;
        }

        var split = pattern.split(" ");
        if (split.length == 0) {
            return "";
        }
        var result = split[0];
        if (result.startsWith("/")) {
            result = result.replace("/", "");
        }
        return result;
    }

    private CommandBuilder getBuilder(String targetCommand, CommandBuilder mainBuilder) {
        if (targetCommand.isEmpty()) {
            return mainBuilder;
        }
        if (targetCommand.equalsIgnoreCase(mainBuilder.properties().name()))
            return mainBuilder;

        return mainBuilder.addSubCommand(targetCommand);
    }

    private <T extends Annotation> ActionResult<T> getAnnotation(AnnotatedElement target, Class<T> annotationType) {
        if (target.isAnnotationPresent(annotationType)) {
            return ActionResult.success(target.getAnnotation(annotationType));
        }
        return ActionResult.failed();
    }


}
