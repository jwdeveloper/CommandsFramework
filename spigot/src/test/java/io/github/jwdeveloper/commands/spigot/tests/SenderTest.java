package io.github.jwdeveloper.commands.spigot.tests;

import io.github.jwdeveloper.commands.spigot.tests.common.SpigotTestBase;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

/* Created by Conor on 10.09.2024 */
public class SenderTest extends SpigotTestBase {

    @Test
    public void testRegisteredSenders() {
        var cmd = api.create("/t <name:Text> <age:Number>")
                .withExcludedSenders(BlockCommandSender.class)
                .onPlayerExecute(event -> System.out.println("Invoked"))
                .build();

        final var result = cmd.executeCommand(playerSender, "", "c", "1");
        assertTrue(result);
    }

    @Test
    public void testUnregisteredSenders() {
        api.commandSenders().unregister(BlockCommandSender.class);

        var cmd = api.create("/t <name:Text> <age:Number>")
                .withExcludedSenders(BlockCommandSender.class)
                .onPlayerExecute((p) -> System.out.println("Player invoked") )
                .onBlockExecute((b) -> System.out.println("Block invoked") )    // Shouldn't happen
                .build();

        final var result = cmd.executeCommand(blockSender, "", "c", "1");
        assertFalse(result);
    }

    @Test
    public void testReferencedExecution() {
        // Conor-11.09.2024: Can't be tested rn
        var cmd = api.create("/t <name:Text> <age:Number>")
                .onExecute(Player.class, event -> System.out.println("Invoked"))
                .build();

        final var result = cmd.executeCommand(blockSender, "", "c", "1");
        assertTrue(result);
    }

}
