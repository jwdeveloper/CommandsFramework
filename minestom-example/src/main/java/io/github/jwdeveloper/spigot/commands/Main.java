package io.github.jwdeveloper.spigot.commands;

import io.github.jwdeveloper.commands.minestom.CommandsFramework;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.listener.manager.PacketListenerManager;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.play.ClientTabCompletePacket;
import net.minestom.server.network.packet.server.play.TabCompletePacket;
import net.minestom.server.utils.chunk.ChunkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) throws IOException {

        var server = MinecraftServer.init();

        var instanceManager = MinecraftServer.getInstanceManager();
        var instanceContainer = instanceManager.createInstanceContainer();

        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

        var globalEventHandler = MinecraftServer.getGlobalEventHandler();

        var chunks = new ArrayList<CompletableFuture<Chunk>>();
        ChunkUtils.forChunksInRange(0, 0, 32, (x, z) -> chunks.add(instanceContainer.loadChunk(x, z)));

        PacketListenerManager packetListenerManager = MinecraftServer.getPacketListenerManager();
        packetListenerManager.setListener(ConnectionState.STATUS, ClientTabCompletePacket.class, (packet, player) -> {

            var transaction = packet.transactionId();
            var input = packet.text();

            var command = CommandsFramework.api().findByName("x").get();
            var suggestions = command.executeSuggestions(packet, "", input.split(" "));
            if (suggestions.isFailed()) {
                return;
            }
            var result = suggestions.getValue().stream().map(e ->
            {
                return new TabCompletePacket.Match(e, Component.text(""));
            }).toList();

            var res = new TabCompletePacket(transaction, 0, result.size(), result);
            player.sendPacket(res);
        });

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });
        server.start("0.0.0.0", 25565);
        var commands = CommandsFramework.enable();
        commands.create("/hello <arg1>")
                .onExecute(event ->
                {
                    System.out.println("Thank you for calling this command!");
                })
                .register();

        var listener = new CommandsListenerThread(MinecraftServer.getCommandManager(), server);
        var consoleCommandsThread = new Thread(listener);
        consoleCommandsThread.setName("commands-thread");
        consoleCommandsThread.start();
    }

}
