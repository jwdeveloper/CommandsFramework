package io.github.jwdeveloper.spigot.commands.impl;

import io.github.jwdeveloper.commands.api.exceptions.PatternException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class PatternsTests extends CommandsTestBase {

    @Test
    public void should_register_pattern() {

        AtomicInteger counter = new AtomicInteger();
        api.patterns()
                .registerForCommand("test", (value, builder, source) ->
                {
                    counter.incrementAndGet();
                })
                .registerForArgument("test", (value, builder, source) -> {
                    counter.incrementAndGet();
                });
        create("/test(test:123) <arg1(test:123)>").register();
        Assertions.assertEquals(2, counter.get());
    }


    @Test
    public void should_override_symbol() {

        AtomicInteger counter = new AtomicInteger();
        api.patterns()
                .registerForCommand("default", (value, builder, source) ->
                {
                    counter.incrementAndGet();
                    System.out.println("The default implementation");
                })
                .registerForCommand("default", (value, builder, source) -> {
                    counter.incrementAndGet();
                    System.out.println("The override implementation");
                });
        create("/test(default:123) <arg1>").register();
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    public void should_throw_when_invalid_syntax() {
        Assertions.assertThrows(PatternException.class, () ->
        {
            create("/test(default:123) <arg1").register();
        });
    }

    @Test
    public void should_throw_when_invalid_character() {
        Assertions.assertThrows(PatternException.class, () ->
        {
            create("/test(default:123) <arg1@").register();
        });
    }
}
