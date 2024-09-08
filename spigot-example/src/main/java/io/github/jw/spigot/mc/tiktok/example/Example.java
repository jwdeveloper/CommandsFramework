package io.github.jw.spigot.mc.tiktok.example;

import io.github.jwdeveloper.commands.spigot.CommandsFramework;
import io.github.jwdeveloper.commands.spigot.api.SpigotCommands;
import io.github.jwdeveloper.commands.api.annotations.FAction;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public final class Example extends JavaPlugin {

    @Override
    public void onEnable() {

        SpigotCommands commands = CommandsFramework.enable(this);


        commands.argumentTypes()
                .createEnum("Attribute", Attribute.class)
                .register();

        commands.create("/test <entity:Entity(ds)> <name:Text> <ischild:Bool(ds)>")
                .onPlayerExecute(event ->
                {
                    var entityType = event.getArgument("entity", EntityType.class);
                    var isChild = event.getBool("ischild");
                    event.sender().sendMessage("type: " + entityType + isChild);
                })
                .register();
    }

    @FAction(identifier = "parse")
    public void defaultCommandParser(ArgumentParseEvent event) {

    }
}
