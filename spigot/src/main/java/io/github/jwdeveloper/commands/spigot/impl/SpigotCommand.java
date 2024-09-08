package io.github.jwdeveloper.commands.spigot.impl;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpigotCommand extends org.bukkit.command.Command {

    @Getter
    io.github.jwdeveloper.commands.api.Command fluentFrameworkCommand;

    protected SpigotCommand(String name, io.github.jwdeveloper.commands.api.Command command) {
        super(name);
        this.fluentFrameworkCommand = command;
        updateProperties();
    }



    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        return fluentFrameworkCommand.executeCommand(commandSender, label, args).isSuccess();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        var result = fluentFrameworkCommand.executeSuggestions(sender, alias, args);
        if (result.isFailed()) {
            return Collections.emptyList();
        }
        return result.getValue();
    }

    public void updateProperties() {

        var properties = fluentFrameworkCommand.properties();
        if (properties.aliases() != null)
            super.setAliases(Arrays.stream(properties.aliases()).toList());
        super.setPermission(properties.permission());
        super.setLabel(properties.label());
        super.setDescription(properties.description());
        super.setUsage(properties.usageMessage());
    }
}
