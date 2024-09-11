package io.github.jwdeveloper.commands.spigot.api;

import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.data.CommandProperties;
import io.github.jwdeveloper.commands.api.functions.CommandEventInvoker;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface SpigotCommandBuilder extends CommandBuilder<SpigotCommandBuilder, CommandSender> {

    // Conor-10.09.2024: Is this correct?! Otherwise, I get a NPE when trying to use #withExcludedSenders,
    // because CommandBuilder#withProperties returns null
    @Override
    default SpigotCommandBuilder withProperties(Consumer<CommandProperties> properties) {
        return this;
    }

    /**
     * Registers an event action to be executed when a command is run by a ProxiedCommandSender.
     *
     * @param action The action to be executed.
     * @return The builder instance.
     */
    default SpigotCommandBuilder onProxyExecute(CommandEventInvoker<ProxiedCommandSender> action) {
        return onExecute(ProxiedCommandSender.class, action);
    }

    /**
     * Registers an event action to be executed when a command is run by a RemoteConsoleCommandSender.
     *
     * @param action The action to be executed.
     * @return The builder instance.
     */
    default SpigotCommandBuilder onRemoteConsoleExecute(CommandEventInvoker<RemoteConsoleCommandSender> action) {
        return onExecute(RemoteConsoleCommandSender.class, action);
    }

    /**
     * Registers an event action to be executed when a command is run by a BlockCommandSender.
     *
     * @param action The action to be executed.
     * @return The builder instance.
     */
    default SpigotCommandBuilder onBlockExecute(CommandEventInvoker<BlockCommandSender> action) {
        return onExecute(BlockCommandSender.class, action);
    }

    /**
     * Registers an event action to be executed when a command is run by a Player.
     *
     * @param action The action to be executed.
     * @return The builder instance.
     */
    default SpigotCommandBuilder onPlayerExecute(CommandEventInvoker<Player> action) {
        return onExecute(Player.class, action);
    }

    /**
     * Registers an event action to be executed when a command is run by a ConsoleCommandSender.
     *
     * @param action The action to be executed.
     * @return The builder instance.
     */
    default SpigotCommandBuilder onConsoleExecute(CommandEventInvoker<ConsoleCommandSender> action) {
        return onExecute(ConsoleCommandSender.class, action);
    }
}
