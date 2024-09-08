package io.github.jwdeveloper.commands.spigot.tests;

import io.github.jwdeveloper.commands.spigot.tests.common.SpigotTestBase;
import org.junit.jupiter.api.Test;


public class ExampleTest extends SpigotTestBase {


    @Test
    public void test() {

        var command = api.create("/join")
                .addArgument("name")
                .onPlayerExecute(event ->
                {
                    System.out.println("Player invoked!");
                })
                .onExecute(event ->
                {
                    System.out.println("Say hello");
                })
                .register();
        var result = command.executeCommand(playerSender, "", "siema");

    }

}
