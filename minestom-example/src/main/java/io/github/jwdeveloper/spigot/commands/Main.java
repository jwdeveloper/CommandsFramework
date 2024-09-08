package io.github.jwdeveloper.spigot.commands;

import io.github.jwdeveloper.commands.minestom.CommandsFramework;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.chunk.ChunkUtils;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {
        var server = MinecraftServer.init();

        var instanceManager = MinecraftServer.getInstanceManager();
        var instanceContainer = instanceManager.createInstanceContainer();

        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

        var globalEventHandler = MinecraftServer.getGlobalEventHandler();

        var chunks = new ArrayList<CompletableFuture<Chunk>>();
        ChunkUtils.forChunksInRange(0, 0, 32, (x, z) -> chunks.add(instanceContainer.loadChunk(x, z)));

        CompletableFuture.runAsync(() -> {
            CompletableFuture.allOf(chunks.toArray(CompletableFuture[]::new)).join();
            System.out.println("load end");
            LightingChunk.relight(instanceContainer, instanceContainer.getChunks());
            System.out.println("light end");
        });

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });


        // Start the server on port 25565
        server.start("0.0.0.0", 25565);

        CommandsFramework.enable();
    }

}
