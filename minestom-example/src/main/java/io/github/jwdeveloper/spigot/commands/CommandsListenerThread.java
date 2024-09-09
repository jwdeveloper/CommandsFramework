package io.github.jwdeveloper.spigot.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.ConsoleSender;

import java.util.Scanner;

public class CommandsListenerThread implements Runnable {

    CommandManager commandManager;
    MinecraftServer minecraftServer;

    public CommandsListenerThread(CommandManager commandManager, MinecraftServer minecraftServer) {
        this.commandManager = commandManager;
        this.minecraftServer = minecraftServer;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            var input = scanner.nextLine();
            if (input.startsWith("/")) {
                input = input.substring(1);
            }
            commandManager.execute(new ConsoleSender(), input);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                scanner.close();
                throw new RuntimeException(e);
            }
        }
    }
}
