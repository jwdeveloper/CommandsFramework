package io.github.jwdeveloper.spigot.commands.impl.common;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.CommandsRegistry;

import java.util.ArrayList;
import java.util.List;

public class CommandsRegistryMock implements CommandsRegistry {


    List<Command> commands = new ArrayList<>();

    @Override
    public boolean add(Command command) {
        return commands.add(command);
    }

    @Override
    public boolean remove(Command command) {
        return commands.remove(command);
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public void removeAll() {
        commands.clear();
    }
}
