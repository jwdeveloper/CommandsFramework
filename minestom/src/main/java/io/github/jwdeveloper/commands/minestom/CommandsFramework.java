package io.github.jwdeveloper.commands.minestom;

import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentsTypesRegistry;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.exceptions.ArgumentException;
import io.github.jwdeveloper.commands.core.CommandFrameworkBuilder;
import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.registry.StaticProtocolObject;

import java.util.Collection;
import java.util.function.Consumer;

public class CommandsFramework {

    private static MinestomCommands commands;

    public static boolean isEnabled() {
        return commands != null;
    }

    public static MinestomCommands api() {
        if (!isEnabled()) {
            throw new RuntimeException("MinestomCommands has not been enabled");
        }
        return commands;
    }

    public static void disable() {
        if (!isEnabled()) {
            throw new RuntimeException("MinestomCommands has not been enabled");
        }
        api().removeAll();
        commands = null;
    }

    public static MinestomCommands enable() {
        return enable(x -> {
        });
    }

    public static MinestomCommands enable(Consumer<DependanceContainerBuilder> action) {
        commands = (MinestomCommands) CommandFrameworkBuilder.create(container ->
        {
            container.registerSingleton(CommandsRegistry.class, MinestomCommandsRegistry.class);

            container.registerProxy(CommandBuilder.class, MinestomCommandBuilder.class);
            container.registerProxy(Commands.class, MinestomCommands.class);

            action.accept(container);
        });

        var argumentTypes = commands.argumentTypes();
        try {
            registerMinestomType(argumentTypes, Block.class);
            registerMinestomType(argumentTypes, Material.class);
            registerMinestomType(argumentTypes, EntityType.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return commands;
    }

    private static void registerMinestomType(ArgumentsTypesRegistry builder, Class type) throws NoSuchMethodException {

        var namespaceMethod = type.getMethod("fromNamespaceId", type);
        var valuesMethod = type.getMethod("values", type);
        builder.create(type.getSimpleName())
                .onParse(event ->
                {
                    try {
                        var input = event.nextArgument();
                        return namespaceMethod.invoke(null, input);
                    } catch (Exception e) {
                        throw new ArgumentException(e);
                    }
                })
                .onSuggestion(event ->
                {
                    try {
                        var collection = (Collection) valuesMethod.invoke(null);
                        return collection.stream().map(e ->
                        {
                            var a = (StaticProtocolObject) e;
                            return a.name();
                        }).toList();
                    } catch (Exception e) {
                        throw new ArgumentException(e);
                    }
                }).register();
    }

}
