package io.github.jwdeveloper.commands.spigot.api;

import io.github.jwdeveloper.commands.api.Commands;

public interface SpigotCommands extends Commands {
    SpigotCommandBuilder create(String pattern);
    SpigotCommandBuilder create(Object object);
}
