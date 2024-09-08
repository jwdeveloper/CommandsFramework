package io.github.jwdeveloper.spigot.commands.impl.templates;


import io.github.jwdeveloper.commands.api.annotations.FCommand;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandErrorEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandValidationEvent;
import org.bukkit.entity.Player;

import java.util.List;


public class ExampleTemplate {


    @FCommand(
            pattern = "/spawn <name:Text> <coins:Number>",
            description = "default description",
            usageMessage = "/spawn john 12",
            permission = "world.commands.spawn",
            hideFromCommands = false,
            shortDescription = "This is short description",
            aliases = {"Alias1", "Alias2"},
            onValidation = "validate",
            onError = "error",
            onBuild = "builder")


    public String onDefault(Player sender, String name, double coins) {

        System.out.println("I'm executed");
        var i = 0;
        i = i / 0;

        System.out.println("Default command invoked! " + sender + " " + name + " " + coins + " ");
        return "Default command invoked! " + sender + " " + name + " " + coins + " ";
    }

    private void builder(CommandBuilder builder) {
        System.out.println("I'm builder");
    }

    private boolean validate(CommandValidationEvent event) {
        System.out.println("I'm validated");
        return true;
    }

    private boolean error(CommandErrorEvent event) {
        System.out.println("I'm error");
        return true;
    }

    private List<String> suggest(ArgumentSuggestionEvent event) {
        return List.of("one", "two", "three");
    }
}
