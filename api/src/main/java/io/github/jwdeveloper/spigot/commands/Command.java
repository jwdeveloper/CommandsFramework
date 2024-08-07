package io.github.jwdeveloper.spigot.commands;

import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.spigot.commands.data.ActionResult;
import io.github.jwdeveloper.spigot.commands.data.CommandProperties;
import io.github.jwdeveloper.spigot.commands.argumetns.ArgumentProperties;
import io.github.jwdeveloper.spigot.commands.data.events.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public interface Command {
    CommandProperties properties();

    List<ArgumentProperties> arguments();

    List<Command> children();

    boolean hasChild(String name);

    Optional<Command> child(String name);

    ActionResult<CommandEvent> executeCommand(CommandSender sender, String commandLabel, String... arguments);


    default ActionResult<CommandEvent> executeCommand(String... arguments) {
        return executeCommand(Bukkit.getConsoleSender(), "", arguments);
    }

    ActionResult<List<String>> executeSuggestions(CommandSender sender, String alias, String... args);

    Optional<Command> parent();

    boolean hasParent();

    String name();

    DependanceContainer container();
}
