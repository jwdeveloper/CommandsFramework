package io.github.jwdeveloper.spigot.commands.impl.parsers;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentType;
import io.github.jwdeveloper.commands.core.impl.parsers.BoolParser;
import io.github.jwdeveloper.spigot.commands.impl.common.ParserTestBase;
import org.junit.jupiter.api.Test;

public class BoolParserTests extends ParserTestBase {
    @Override
    public ArgumentType getParser() {
        return new BoolParser();
    }


    @Test
    public void should_parse() {
        var result = parse("true");
        assertTrue(result);
        assertValue(result, true);
    }

    @Test
    public void should_parse_false() {
        var result = parse("false");
        assertTrue(result);
        assertValue(result, false);
    }

        @Test
    public void should_parse_capital_letter() {
        var result = parse("TrUE");
        assertTrue(result);
        assertValue(result, true);
    }
}
