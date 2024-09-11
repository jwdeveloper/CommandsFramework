package io.github.jwdeveloper.commands.spigot;

import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.services.ValidationService;
import io.github.jwdeveloper.commands.core.CommandFrameworkBuilder;
import io.github.jwdeveloper.commands.spigot.api.SpigotCommandBuilder;
import io.github.jwdeveloper.commands.spigot.api.SpigotCommands;
import io.github.jwdeveloper.commands.spigot.impl.SpigotCommandsRegistry;
import io.github.jwdeveloper.commands.spigot.impl.PluginDisableListener;
import io.github.jwdeveloper.commands.spigot.impl.services.SpigotValidationService;
import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;
import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import io.github.jwdeveloper.commands.spigot.impl.parsers.LocationParser;
import io.github.jwdeveloper.commands.core.impl.parsers.NumberParser;
import io.github.jwdeveloper.commands.spigot.impl.parsers.PlayerParser;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class CommandsFramework {

    private static SpigotCommands commands;

    public static SpigotCommands enable(Plugin plugin, Consumer<DependanceContainerBuilder> action) {
        commands = (SpigotCommands) CommandFrameworkBuilder.create(container ->
        {
            container.registerSingleton(Plugin.class, plugin);
            container.registerSingleton(PluginDisableListener.class);
            container.registerSingleton(CommandsRegistry.class, SpigotCommandsRegistry.class);
            container.registerSingleton(ValidationService.class, SpigotValidationService.class);

            /**
             * Overriding the default implementation of Commands, and CommandsBuilder
             * with proxy.
             *
             * Proxy class can use the parent methods and own methods. Own
             * methods must have modifier `default` to be detected
             */
            container.registerProxy(CommandBuilder.class, SpigotCommandBuilder.class);
            container.registerProxy(Commands.class, SpigotCommands.class);

            action.accept(container);
        });
        var senders = commands.commandSenders();
        senders.register(Player.class);
        senders.register(ConsoleCommandSender.class);
        senders.register(ProxiedCommandSender.class);
        senders.register(BlockCommandSender.class);
        senders.register(RemoteConsoleCommandSender.class);

        var argumentTypes = commands.argumentTypes();

        argumentTypes.register(new PlayerParser());
        argumentTypes.register(new LocationParser(new NumberParser()));
        argumentTypes.createEnum("Entity", EntityType.class).register();
        argumentTypes.createEnum("Sound", Sound.class).register();
        argumentTypes.createEnum("Particle", Particle.class).register();
        argumentTypes.createEnum("Color", ChatColor.class).register();
        argumentTypes.createEnum("Material", Material.class).register();

        var listener = commands.container().find(PluginDisableListener.class);
        Bukkit.getPluginManager().registerEvents(listener, plugin);

        return commands;
    }

    public static boolean isEnabled() {
        return commands != null;
    }

    public static SpigotCommands api() {
        if (!isEnabled()) {
            throw new RuntimeException("Fluent commands has not been enabled");
        }
        return commands;
    }

    public static void disable() {
        if (!isEnabled()) {
            throw new RuntimeException("Fluent commands has not been enabled");
        }
        var listener = commands.container().find(PluginDisableListener.class);
        PluginDisableEvent.getHandlerList().unregister(listener);
        api().removeAll();
        commands = null;
    }

    public static SpigotCommands enable(Plugin plugin) {
        return enable(plugin, x -> {
        });
    }
}
