package io.github.jwdeveloper.spigot.commands.impl.common;


import io.github.jwdeveloper.commands.api.data.events.CommandEvent;

import io.github.jwdeveloper.commands.core.CommandFrameworkBuilder;
import io.github.jwdeveloper.commands.api.Command;
import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.commands.api.builders.CommandBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;


public abstract class CommandsTestBase {
    protected Commands api;
    protected Object sender;


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
        return find(name).executeCommand(sender, name, args);
    }

    public ActionResult<List<String>> executeSuggestions(String name, String... args) {
        return find(name).executeSuggestions(sender, name, args);
    }

    @BeforeEach
    public void setUp() {
        sender = this;
        api = CommandFrameworkBuilder.create(b -> {
        });
        onBefore(api);
    }

}
