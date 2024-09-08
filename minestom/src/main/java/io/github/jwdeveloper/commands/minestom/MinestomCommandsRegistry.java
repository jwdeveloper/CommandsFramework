package io.github.jwdeveloper.commands.minestom;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;

import java.util.List;

public class MinestomCommandsRegistry implements CommandsRegistry {

    CommandManager commandManager;

    public MinestomCommandsRegistry()
    {
        commandManager = MinecraftServer.getCommandManager();
    }

    @Override
    public boolean add(Command command) {
        return false;
    }

    @Override
    public boolean remove(Command command) {
        return false;
    }

    @Override
    public List<Command> commands() {
        return List.of();
    }

    @Override
    public void removeAll() {

    }
}
