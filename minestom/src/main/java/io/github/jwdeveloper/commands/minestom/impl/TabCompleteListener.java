package io.github.jwdeveloper.commands.minestom.impl;

import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import io.github.jwdeveloper.commands.minestom.CommandsFramework;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.play.ClientTabCompletePacket;
import net.minestom.server.network.packet.server.play.TabCompletePacket;
import net.minestom.server.network.player.PlayerConnection;

public class TabCompleteListener {
    private final Commands commands;

    public TabCompleteListener(Commands commands) {
        this.commands = commands;
    }

    public void enable() {
        var packetListener = MinecraftServer.getPacketListenerManager();
        packetListener.setListener(
                ConnectionState.STATUS,
                ClientTabCompletePacket.class,
                this::handleTabComplete);
    }

    private void handleTabComplete(ClientTabCompletePacket packet, PlayerConnection player) {
        var transaction = packet.transactionId();
        var input = packet.text();

        var commandName = Utils.getCommandName(input);
        var commandArgs = Utils.getArguments(input);

        var optional = commands.findByName(commandName);
        if (optional.isEmpty()) {
            return;
        }
        var command = optional.get();
        var suggestions = command.executeSuggestions(packet, commandName, commandArgs);
        if (suggestions.isFailed()) {
            return;
        }

        var matches = suggestions
                .getValue()
                .stream()
                .map(e -> new TabCompletePacket.Match(e, Component.text("")))
                .toList();

        var outputPacket = new TabCompletePacket(transaction, 0, matches.size(), matches);
        player.sendPacket(outputPacket);
    }
}
