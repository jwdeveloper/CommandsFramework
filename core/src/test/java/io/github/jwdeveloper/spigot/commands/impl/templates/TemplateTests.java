package io.github.jwdeveloper.spigot.commands.impl.templates;

import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.spigot.commands.impl.CommandsTestBase;
import org.junit.jupiter.api.Test;

public class TemplateTests extends CommandsTestBase {

    @Test
    public void shouldParseTempalte() {
        var template = new ExampleTemplate();
        var command = api.create(template).build();

        var result = execute("spawn", "john", "122");

        var i = 0;
    }

    @Override
    protected void onBefore(Commands commands) {

    }
}
