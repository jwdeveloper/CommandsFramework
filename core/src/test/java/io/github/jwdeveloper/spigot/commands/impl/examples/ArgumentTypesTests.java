package io.github.jwdeveloper.spigot.commands.impl.examples;

import io.github.jwdeveloper.spigot.commands.impl.CommandsTestBase;
import io.github.jwdeveloper.commands.api.exceptions.ArgumentException;
import org.bukkit.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ArgumentTypesTests extends CommandsTestBase {


    @Test
    public void custom_argument_should_work() {
        api.argumentTypes()
                .create("Sword")
                .onParse(argumentParseEvent ->
                {
                    var name = argumentParseEvent.nextArgument();
                    name = name.replace(" ", "_").toUpperCase();
                    var material = Material.getMaterial(name);
                    if (!material.isItem()) {
                        throw new ArgumentException("Must be item!");
                    }
                    return material;
                })
                .onSuggestion(argumentSuggestionEvent ->
                        Stream.of(Material.DIAMOND_SWORD, Material.GOLDEN_SWORD)
                                .map(Enum::name)
                                .toList())
                .register();

        var command = create("/give <item:Sword(ds)>").register();
        var event = execute(command.name(), "Diamond_sword");
        assertTrue(event);

        Assertions.assertEquals(1, event.getValue().argumentCount());
        var materialOutput = event.getValue().getArgument("item", Material.class);
        Assertions.assertEquals(Material.DIAMOND_SWORD, materialOutput);

        var suggestions = executeSuggestions(command.name(), "");
        assertTrue(suggestions);
        Assertions.assertEquals("DIAMOND_SWORD", suggestions.getValue().get(0));
        Assertions.assertEquals("GOLDEN_SWORD", suggestions.getValue().get(1));
    }

    @Test
    public void default_argument_should_be_overided() {
        var customText = "This is the custom text!";
        var parseResult = "Test value";
        api.argumentTypes()
                .create("Text")
                .onParse(argumentParseEvent -> parseResult)
                .onSuggestion(argumentSuggestionEvent -> List.of(customText))
                .register();

        var command = create("/say <word:Text(ds)>").register();
        var suggestions = executeSuggestions(command.name(), "");
        assertTrue(suggestions);
        Assertions.assertEquals(customText, suggestions.getValue().get(0));

        var result = execute(command.name(), "Hello", "");
        assertTrue(result);
        Assertions.assertEquals(parseResult, result.getValue().getString("word"));
    }


    @Test
    public void should_works_for_stocks() {

        var stocks = new ArrayList<String>();
        stocks.add("GOOGLE");
        stocks.add("NVIDIA");
        stocks.add("NASDACK");
        stocks.add("APPLE");


        api.argumentTypes()
                .create("Stock")
                .onSuggestion(argumentSuggestionEvent ->
                {
                    var value = argumentSuggestionEvent.rawValue();
                    var result = stocks.stream().filter(e -> e.toLowerCase().contains(value.toLowerCase())).toList();
                    return result;
                })
                .onParse(argumentParseEvent ->
                {
                    var input = argumentParseEvent.nextArgument();
                    var optional = stocks.stream().filter(e -> e.equals(input)).findFirst();
                    return optional.orElseThrow(() -> new RuntimeException("Stock not found!"));
                })
                .register();

        create("/stock <action[buy,sell]> <stock:Stock(ds)> <amount:Number>").register();

        var result = execute("stock", "buy", "NVIDIA", "12");
        assertTrue(result);


        var suggestions_stock = executeSuggestions("stock", "buy", "NVI");
        assertTrue(suggestions_stock);
        Assertions.assertEquals("NVIDIA", suggestions_stock.getValue().get(0));

        var suggestions = executeSuggestions("stock", "buy", "NVIDIA", "1");
        assertTrue(suggestions);
        Assertions.assertEquals("<amount:Number>", suggestions.getValue().get(0));
    }
}
