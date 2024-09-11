package io.github.jwdeveloper.commands.core.impl;

import io.github.jwdeveloper.commands.api.services.CommandSenderRegistry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* Created by Conor on 10.09.2024 */
public class DefaultCommandSenderRegistry implements CommandSenderRegistry {

    private final Set<Class<?>> commandSenders = new HashSet<>();

    @Override
    public void register(Class<?> commandSenderClazz) {
       commandSenders.add(commandSenderClazz);
    }

    @Override
    public void unregister(Class<?> commandSenderClazz) {
        commandSenders.remove(commandSenderClazz);
    }

    @Override
    public Set<Class<?>> getCommandSenders() {
        return Collections.unmodifiableSet(commandSenders);
    }
}
