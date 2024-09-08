package io.github.jwdeveloper.spigot.commands.impl.parsers;


import io.github.jwdeveloper.commands.core.impl.patterns.PatternTokenizer;
import org.junit.jupiter.api.Test;

public class TokenIteratorTests {

    @Test
    public void parse() {
        var command = "/hello <number[one,two,three]>";

        var iterator = new PatternTokenizer(command);
        iterator.nextOrThrow("/");
        iterator.nextOrThrow("hello");
        iterator.nextOrThrow("<");
        iterator.nextOrThrow("number");
        iterator.nextOrThrow("[");
        iterator.nextOrThrow("one");
        iterator.nextOrThrow(",");
        iterator.nextOrThrow("two");
        iterator.nextOrThrow(",");
        iterator.nextOrThrow("three");
        iterator.nextOrThrow("]");
        iterator.nextOrThrow(">");
    }


    @Test
    public void parseName() {
        var command = "/hello/world";

        var iterator = new PatternTokenizer(command);
        iterator.nextOrThrow("/");
        iterator.nextOrThrow("hello");
        iterator.nextOrThrow("/");
        iterator.nextOrThrow("world");

    }
}
