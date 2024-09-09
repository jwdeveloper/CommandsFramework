package io.github.jwdeveloper.commands.minestom.api;

import io.github.jwdeveloper.commands.api.Commands;

public interface MinestomCommands extends Commands {
    MinestomCommandBuilder create(String pattern);

    MinestomCommandBuilder create(Object object);

}
