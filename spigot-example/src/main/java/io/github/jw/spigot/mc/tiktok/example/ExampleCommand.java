package io.github.jw.spigot.mc.tiktok.example;

import io.github.jwdeveloper.commands.api.annotations.FCommand;
import io.github.jwdeveloper.commands.api.data.events.CommandErrorEvent;
import io.github.jwdeveloper.commands.api.data.events.CommandValidationEvent;
import io.github.jwdeveloper.commands.spigot.api.SpigotCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExampleCommand {

    @FCommand(pattern = "/hello <name:Text> <points:Number> <job:Text[Miner,Warrior,Fisherman]>",
            onBuild = "onBuild",
            onValidation = "onValidation",
            onError = "onError")
    public void helloCommand(Player player, String name, double points, String job) {
        player.sendMessage(ChatColor.DARK_GREEN + "Name: " + name + " points: " + points + " job" + job);
        int i = 0;
        i = i / 0; //Forcing exception
    }


    public void onBuild(SpigotCommandBuilder builder) {
        builder.argument("name").withDefaultValue("john");
    }

    public boolean onValidation(CommandValidationEvent<CommandSender> event) {

        System.out.println("=========================");
        for (var arg : event.args()) {
            System.out.println(arg);
        }

        event.sender().sendMessage(ChatColor.YELLOW + "Validating command: " + event.args().length);
        return true;
    }

    public void onError(CommandErrorEvent<CommandSender> event) {
        event.exception().printStackTrace();
        event.sender().sendMessage(ChatColor.RED + "Handled exception: " + event.exception().getCause().getMessage());
    }

}
