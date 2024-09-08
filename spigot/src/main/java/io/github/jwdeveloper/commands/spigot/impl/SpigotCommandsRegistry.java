package io.github.jwdeveloper.commands.spigot.impl;

import io.github.jwdeveloper.commands.core.impl.DefaultCommand;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.CommandsRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpigotCommandsRegistry implements CommandsRegistry {
    private final Map<String, SpigotCommand> commands;
    private final SimpleCommandMap commandMap;
    private final Plugin plugin;

    public SpigotCommandsRegistry(Plugin plugin) {
        commands = new HashMap<>();
        this.plugin = plugin;
        this.commandMap = getCommandMap();
    }

    @Override
    public boolean add(Command command) {
        var name = command.properties().name();
        if (commands.containsKey(name)) {
            plugin.getLogger().info("command already exists " + name);
            return false;
        }

        var spigotCommand = new SpigotCommand(command.properties().name(), command);

        if (!registerBukkitCommand(spigotCommand)) {
            plugin.getLogger().info("unable to register command " + name);
            return false;
        }
        commands.put(name, spigotCommand);
        return true;
    }

    @Override
    public boolean remove(Command command) {
        if (!commands.containsKey(command.properties().name())) {
            return false;
        }
        var spigotCommand = commands.get(command.properties().name());
        return unregisterBukkitCommand(spigotCommand);
    }


    @Override
    public void removeAll() {

        for (var command : commands.values().stream().toList()) {
            unregisterBukkitCommand(command);
        }
    }


    private boolean registerBukkitCommand(SpigotCommand simpleCommand) {
        try {
            var result = commandMap.register(plugin.getName(), simpleCommand);
            updateBukkitCommands();
            return result;
        } catch (Exception e) {
            plugin.getLogger().info("Unable to register command " + simpleCommand.getName());
            return false;
        }
    }

    private boolean unregisterBukkitCommand(SpigotCommand command) {
        var name = command.getFluentFrameworkCommand().properties().name();
        try {
            var field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            var map = field.get(commandMap);
            field.setAccessible(false);
            var knownCommands = (HashMap<String, org.bukkit.command.Command>) map;
            knownCommands.remove(name, command);
            knownCommands.remove(plugin.getName() + ":" + name, command);
            command.unregister(commandMap);
            for (String alias : command.getAliases()) {
                if (!knownCommands.containsKey(alias))
                    continue;

                if (!knownCommands.get(alias).toString().contains(name)) {
                    continue;
                }
                knownCommands.remove(alias);
            }
            updateBukkitCommands();
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Unable to unregister command " + name);
            return false;
        }
    }

    private void updateBukkitCommands() throws Exception {
        var server = Bukkit.getServer();
        var syncCommand = Arrays.stream(server.getClass().getMethods()).filter(e -> e.getName().equals("syncCommands")).findFirst();
        if (syncCommand.isPresent()) {
            var syncMethod = syncCommand.get();
            syncMethod.invoke(Bukkit.getServer());
        }
    }

    @Override
    public List<Command> commands() {
        return commands.values().stream().map(e -> (Command) e).toList();
    }


    public List<String> getBukkitCommandsNames() {
        return commandMap.getCommands().stream().map(org.bukkit.command.Command::getName).toList();
    }

    public List<org.bukkit.command.Command> getBukkitCommands() {
        return commandMap.getCommands().stream().toList();
    }


    private SimpleCommandMap getCommandMap() {


        if (Bukkit.getServer() == null) {
            return null;
        }

        try {
            return (SimpleCommandMap) getPrivateField(Bukkit.getServer(), "commandMap");
        } catch (Exception e) {
            throw new RuntimeException("Unable to get the Simple command map!", e);
        }
    }

    public static Object getPrivateField(Object object, String field) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

}
