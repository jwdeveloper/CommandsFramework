package io.github.jwdeveloper.commands.spigot.tests.templates;

import io.github.jwdeveloper.commands.api.Commands;

import io.github.jwdeveloper.commands.spigot.tests.common.SpigotTestBase;
import org.junit.jupiter.api.Test;

public class TemplateTests extends SpigotTestBase {

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
