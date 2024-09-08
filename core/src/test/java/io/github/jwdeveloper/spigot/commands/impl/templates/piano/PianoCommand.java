package io.github.jwdeveloper.spigot.commands.impl.templates.piano;

import io.github.jwdeveloper.commands.api.annotations.FCommand;
import org.bukkit.entity.Player;

@FCommand(pattern = "/piano <piano:Piano(ds)>")
public class PianoCommand {


    @FCommand
    public void onInvoke(Player player, Piano piano) {
        System.out.println("This was invoked by the player!");
    }

    @FCommand(pattern = "/teleport <x:Number> <y:Number> <z:Number>")
    private void onTeleport(Player player, Piano piano, int location) {
        player.sendMessage("You are being teleported to the piano " + piano.name());
    }

    @FCommand(pattern = "/destroy")
    void onDestroy(Player player, Piano piano) {
        player.sendMessage("You are being destroyed to the piano " + piano.name());
    }

}
