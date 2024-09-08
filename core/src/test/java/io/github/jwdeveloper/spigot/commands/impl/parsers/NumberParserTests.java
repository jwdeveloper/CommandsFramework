package io.github.jwdeveloper.spigot.commands.impl.parsers;

import io.github.jwdeveloper.commands.core.impl.parsers.NumberParser;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentType;
import io.github.jwdeveloper.spigot.commands.impl.common.ParserTestBase;
import org.junit.jupiter.api.Test;

public class NumberParserTests extends ParserTestBase {

    @Override
    public ArgumentType getParser() {
        return new NumberParser();
    }
    @Test
    public void should_parse() {
        var result = parse("1.0");
        assertTrue(result);
        assertValue(result, 1.0d);
    }


    @Test
    public void should_not_parse_multiple_dots() {
        var result = parse("1.0.0.0.0");
        assertFalse(result, "multiple points");
    }

    @Test
    public void should_not_parse_text() {
        var result = parse("1world");
        assertFalse(result, "It's number, not text!");
    }


    @Test
    public void should_parse_with_comma() {
        var result = parse("1,0");
        assertTrue(result);
        assertValue(result, 1.0d);
    }



}
