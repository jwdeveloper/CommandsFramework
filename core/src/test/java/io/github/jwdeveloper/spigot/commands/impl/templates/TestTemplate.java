package io.github.jwdeveloper.spigot.commands.impl.templates;

import io.github.jwdeveloper.commands.api.annotations.FArgument;
import io.github.jwdeveloper.commands.api.annotations.FCommand;
import io.github.jwdeveloper.commands.api.data.DisplayAttribute;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TestTemplate {


    @FArgument(
            name = "name",
            type = "Text",
            displayAttributes = {DisplayAttribute.NAME, DisplayAttribute.ERROR},
            defaultValue = "john",
            description = "This argument takes name",
            allowNullOutput = false,
            onSuggestions = "suggest",
            onParse = "parse")
    @FArgument(name = "age", type = "Number")
    @FCommand(name = "hello")
    public void onTestCommand(CommandSender sender, String name, double age) {
        sender.sendMessage("Hello " + name + " " + age);
    }


    private List<String> suggest(ArgumentSuggestionEvent event) {
        return List.of("john", "adam", "mike");
    }

    private boolean parse(ArgumentParseEvent event) {

        var argument = event.nextArgument();
        if (argument.isEmpty()) {
            return false;
        }
        return true;
    }
}
