package io.github.jwdeveloper.commands.spigot.tests.common;

import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.data.events.CommandEvent;
import io.github.jwdeveloper.commands.api.services.ValidationService;
import io.github.jwdeveloper.commands.core.impl.DefaultCommandsRegistry;
import io.github.jwdeveloper.commands.spigot.CommandsFramework;
import io.github.jwdeveloper.commands.spigot.api.SpigotCommandBuilder;
import io.github.jwdeveloper.commands.spigot.api.SpigotCommands;
import io.github.jwdeveloper.commands.spigot.impl.PluginDisableListener;
import io.github.jwdeveloper.commands.spigot.impl.SpigotCommandsRegistry;
import io.github.jwdeveloper.commands.spigot.impl.services.SpigotValidationService;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public abstract class SpigotTestBase {

    protected SpigotCommands api;

    protected MockedStatic<Bukkit> bukkitMock;

    protected Player playerSender;
    protected BlockCommandSender blockSender;

    protected void onBefore(Commands commands) {

    }

    public void assertTrue(ActionResult actionResult) {
        Assertions.assertTrue(actionResult.isSuccess());
    }

    public void assertValue(ActionResult actionResult, Object excepted) {
        Assertions.assertTrue(actionResult.isSuccess());
        Assertions.assertEquals(excepted, actionResult.getValue());
    }


    public void assertFalse(ActionResult actionResult) {
        Assertions.assertFalse(actionResult.isSuccess());
    }

    public void assertFalse(ActionResult actionResult, String message) {
        Assertions.assertFalse(actionResult.isSuccess());
        Assertions.assertEquals(message, actionResult.getMessage());
    }


    public Command find(String name) {
        return api.findByName(name).get();
    }


    public CommandBuilder create(String pattern) {
        return api.create(pattern);
    }

    public ActionResult<CommandEvent> execute(String name, String... args) {
        return find(name).executeCommand(playerSender, name, args);
    }

    public ActionResult<List<String>> executeSuggestions(String name, String... args) {
        return find(name).executeSuggestions(playerSender, name, args);
    }

    @BeforeEach
    public void setUp() {
        var plugin = mock(Plugin.class);
        playerSender = mock(Player.class);
        blockSender = mock(BlockCommandSender.class);
        bukkitMock = mockStatic(Bukkit.class);

        // Create mock Server and PluginManager instances
        var serverMock = mock(Server.class);
        var pluginManagerMock = mock(PluginManager.class);

        // Mock the Bukkit.getServer() call to return the mock Server
        bukkitMock.when(Bukkit::getServer).thenReturn(serverMock);
        bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManagerMock);
        api = CommandsFramework.enable(plugin, builder ->
        {
            builder.registerSingleton(CommandsRegistry.class, DefaultCommandsRegistry.class);
            builder.registerSingleton(ValidationService.class, SpigotValidationService.class);
        });
        onBefore(api);
    }

    @AfterEach
    public void onAfter() {
        bukkitMock.reset();
        bukkitMock.clearInvocations();
        bukkitMock.close();
    }

}

