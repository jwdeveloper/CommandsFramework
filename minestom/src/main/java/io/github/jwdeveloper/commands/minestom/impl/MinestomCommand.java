package io.github.jwdeveloper.commands.minestom.impl;

import io.github.jwdeveloper.commands.api.Command;
import lombok.Getter;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinestomCommand extends net.minestom.server.command.builder.Command
        implements CommandExecutor {

    @Getter
    private final io.github.jwdeveloper.commands.api.Command frameworkCommand;

    public MinestomCommand(
            io.github.jwdeveloper.commands.api.Command frameworkCommand,
            @NotNull String name,
            @Nullable String... aliases) {
        super(name, aliases);
        this.frameworkCommand = frameworkCommand;
        setDefaultExecutor(this);
    }

    @Override
    public void apply(@NotNull CommandSender sender, @NotNull CommandContext context) {
        var args = Utils.getArguments(context.getInput());
        frameworkCommand.executeCommand(sender, context.getCommandName(), args);
    }


}
