package io.github.jwdeveloper.commands.minestom.impl;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinestomCommandsRegistry implements CommandsRegistry {
    private final CommandManager commandManager;
    private final Map<String, MinestomCommand> commandsMap;

    public MinestomCommandsRegistry() {
        commandManager = MinecraftServer.getCommandManager();
        commandsMap = new HashMap<>();
    }

    @Override
    public boolean add(Command command) {
        if (commandsMap.containsKey(command.name())) {
            return false;
        }
        var minestomCommand = new MinestomCommand(command, command.name(), command.properties().aliases());
        commandsMap.put(command.name(), minestomCommand);
        commandManager.register(minestomCommand);
        return true;
    }

    @Override
    public boolean remove(Command command) {
        if (!commandsMap.containsKey(command.name())) {
            return false;
        }
        var minestomCommand = commandsMap.get(command.name());
        commandManager.unregister(minestomCommand);
        commandsMap.remove(command.name());
        return true;
    }

    @Override
    public List<Command> commands() {
        return commandsMap.values().stream().map(MinestomCommand::getFrameworkCommand).toList();
    }

    @Override
    public void removeAll() {
        for (var value : commandsMap.values()) {
            remove(value.getFrameworkCommand());
        }
    }
}
