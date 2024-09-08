package io.github.jw.spigot.mc.tiktok.example;

import io.github.jw.spigot.mc.tiktok.example.piano.Piano;
import io.github.jwdeveloper.commands.api.annotations.FCommand;
import org.bukkit.entity.Player;

@FCommand(pattern = "/piano <piano:Piano(ds)>")
public class PianoCommand {



    @FCommand
    public void onInvoke(Player player, Piano piano) {
        System.out.println("This was invoked by the player!");
    }


    @FCommand(pattern = "/teleport <loc:Number>")
    public void onTeleport(Player player, Piano piano, int location)
    {
        player.sendMessage("You are being teleported to the piano " + piano.name());
    }

    @FCommand(pattern = "/destroy")
    public void onDestroy(Player player, Piano piano)
    {
        player.sendMessage("You are being destroyed to the piano " + piano.name());
    }

}
