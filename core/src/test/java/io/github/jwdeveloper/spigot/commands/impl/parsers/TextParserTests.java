package io.github.jwdeveloper.spigot.commands.impl.parsers;

import io.github.jwdeveloper.commands.core.impl.parsers.TextParser;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentType;
import io.github.jwdeveloper.spigot.commands.impl.ParserTestBase;
import org.junit.jupiter.api.Test;

public class TextParserTests extends ParserTestBase {

    @Override
    public ArgumentType getParser() {
        return new TextParser();
    }

    @Test
    public void should_parse() {
        var result = parse("hello");
        assertTrue(result);
        assertValue(result, "hello");
    }

    @Test
    public void should_parse_colon() {
        var result = parse("\"", "hello", "world", "\"");
        assertTrue(result);
        assertValue(result, "hello world");
    }

    @Test
    public void should_parse_single_colon() {
        var result = parse("'", "hello", "this", "is", "message", "'");
        assertTrue(result);
        assertValue(result, "hello this is message");
    }


    @Test
    public void should_not_parse_colon_not_closed() {
        var result = parse("'", "hello", "world");
        assertFalse(result, "Unmatched quotation marks.");
    }

    @Test
    public void should_not_parse_single_one_word_with_one_colon() {
        var result = parse("'This is Word");
        assertFalse(result, "Unmatched quotation marks.");
    }


}
