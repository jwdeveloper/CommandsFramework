package io.github.jwdeveloper.spigot.commands.impl.arguments;

import io.github.jwdeveloper.spigot.commands.impl.common.CommandsTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnumsTests extends CommandsTestBase {


    public enum ExampleEnum {
        ONE, TWO, THREE
    }

    @Test
    public void should_enum_argument() {


        api.argumentTypes()
                .createEnum(ExampleEnum.class)
                .register();

        create("/test")
                .addArgument("test", "ExampleEnum")
                .register();

        var commandResult = execute("test", "ONE");
        assertTrue(commandResult);

        var result = commandResult.getValue().getArgument("test", ExampleEnum.class);
        Assertions.assertEquals(ExampleEnum.ONE, result);

        var suggestionResult = executeSuggestions("test", "");
        assertTrue(suggestionResult);

        Assertions.assertEquals("ONE", suggestionResult.getValue().get(0));
    }


    @Test
    public void should_enum_argument_with_pattern() {
        api.argumentTypes()
                .createEnum(ExampleEnum.class)
                .register();

        create("/test <test:ExampleEnum>")
                .register();

        var commandResult = execute("test", "ONE");
        assertTrue(commandResult);

        var result = commandResult.getValue().getArgument("test", ExampleEnum.class);
        Assertions.assertEquals(ExampleEnum.ONE, result);

        var suggestionResult = executeSuggestions("test", "");
        assertTrue(suggestionResult);

        Assertions.assertEquals("ONE", suggestionResult.getValue().get(0));
    }


}
