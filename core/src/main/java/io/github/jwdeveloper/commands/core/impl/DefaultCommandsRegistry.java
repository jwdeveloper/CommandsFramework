package io.github.jwdeveloper.commands.core.impl;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * The default implementation of the command registry
 */
public class DefaultCommandsRegistry implements CommandsRegistry {
    private final Map<String, DefaultCommand> commands;
    private final Plugin plugin;

    public DefaultCommandsRegistry(Plugin plugin) {
        commands = new HashMap<>();
        this.plugin = plugin;
    }

    @Override
    public boolean add(Command command) {
        var name = command.properties().name();
        if (commands.containsKey(name)) {
            plugin.getLogger().info("command already exists " + name);
            return false;
        }
        commands.put(name, (DefaultCommand) command);
        return true;
    }

    @Override
    public boolean remove(Command command) {
        if (!commands.containsKey(command.properties().name())) {
            return false;
        }
        return unregister((DefaultCommand) command);
    }

    @Override
    public void removeAll() {
        for (var command : commands.values().stream().toList()) {
            unregister(command);
        }
    }


    private boolean unregister(DefaultCommand command) {
        commands.remove(command.properties().name());
        return true;
    }

    @Override
    public List<Command> commands() {
        return commands.values().stream().map(e -> (Command) e).toList();
    }


}
