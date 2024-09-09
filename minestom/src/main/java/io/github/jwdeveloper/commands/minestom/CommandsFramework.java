package io.github.jwdeveloper.commands.minestom;

import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentsTypesRegistry;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.exceptions.ArgumentException;
import io.github.jwdeveloper.commands.api.services.ValidationService;
import io.github.jwdeveloper.commands.core.CommandFrameworkBuilder;
import io.github.jwdeveloper.commands.minestom.api.MinestomCommandBuilder;
import io.github.jwdeveloper.commands.minestom.api.MinestomCommands;
import io.github.jwdeveloper.commands.minestom.impl.MinestomCommandsRegistry;
import io.github.jwdeveloper.commands.minestom.impl.MinestomValidationService;
import io.github.jwdeveloper.commands.minestom.impl.TabCompleteListener;
import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.registry.StaticProtocolObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class CommandsFramework {

    private static MinestomCommands commands;

    public static MinestomCommands enable(Consumer<DependanceContainerBuilder> action) {
        commands = (MinestomCommands) CommandFrameworkBuilder.create(container ->
        {

            container.registerSingleton(TabCompleteListener.class);
            container.registerSingleton(CommandsRegistry.class, MinestomCommandsRegistry.class);
            container.registerSingleton(ValidationService.class, MinestomValidationService.class);


            container.registerProxy(CommandBuilder.class, MinestomCommandBuilder.class);
            container.registerProxy(Commands.class, MinestomCommands.class);

            action.accept(container);
        });


        var tabCompleteListener = commands.container().find(TabCompleteListener.class);
        tabCompleteListener.enable();

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


    /**
     * Register Minestom server type such as Block, Material, EntityType etc..
     * as the command argument
     *
     * @param argumentTypes
     * @param minestomType
     */
    private static void registerMinestomType(ArgumentsTypesRegistry argumentTypes, Class minestomType) {

        /**
         * Method that getting instance of given class from string,
         * in this example, Block
         * Block.fromNamespaceId("Air");
         *
         * Method that returns array of values for given type
         * Block.values();
         */

        var namespaceMethod = Arrays.stream(minestomType.getDeclaredMethods()).filter(e -> e.getName().equals("fromNamespaceId")).findFirst().orElseThrow();
        var valuesMethod = Arrays.stream(minestomType.getDeclaredMethods()).filter(e -> e.getName().equals("values")).findFirst().orElseThrow();
        argumentTypes.create(minestomType.getSimpleName())
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
