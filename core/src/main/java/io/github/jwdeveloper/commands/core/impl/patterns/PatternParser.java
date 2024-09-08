package io.github.jwdeveloper.commands.core.impl.patterns;

import io.github.jwdeveloper.commands.core.impl.data.ArgumentPatternNode;
import io.github.jwdeveloper.commands.core.impl.data.CommandPatternNode;
import io.github.jwdeveloper.dependance.injector.api.util.Pair;
import io.github.jwdeveloper.commands.api.data.ActionResult;

import java.util.ArrayList;
import java.util.List;


/**
 * Parsing the input string pattern into to command node.
 * <p>
 * Syntax
 * <p>
 * [CommandName][Properties(Optional)] [Arguments(Optional)]
 * <p>
 * // CommandName - /name or /name/sub1 or /name/sub1/sub2
 * // Properties - () or (prop1:value) or (prop1) or (prop1,prop2) or (prop1:value1, prop2:value2)
 * // Arguments - one or more Argument
 * // Argument - <name:type>
 */
public class PatternParser {
    private PatternTokenizer iterator;

    public ActionResult<CommandPatternNode> resolve(String pattern) {
        iterator = new PatternTokenizer(pattern);
        return ActionResult.success(command());
    }

    private CommandPatternNode command() {
        var names = names();
        var name = names.get(names.size() - 1);

        var properties = new ArrayList<Pair<String, String>>();
        if (iterator.isNext("(")) {
            properties = properties();
        }
        var arguments = arguments();
        return new CommandPatternNode(name, properties, names, arguments);
    }

    public List<String> names() {

        var names = new ArrayList<String>();
        do {
            if (iterator.isNext("<")) {
                break;
            }
            if (iterator.isNext("(")) {
                break;
            }
            if (iterator.isNext("/")) {
                iterator.next();
            }
            names.add(iterator.next());
        }
        while (iterator.hasNext());

        return names;
    }

    public List<ArgumentPatternNode> arguments() {
        var arguments = new ArrayList<ArgumentPatternNode>();
        while (iterator.hasNext()) {
            iterator.nextOrThrow("<");
            arguments.add(argument());
            iterator.nextOrThrow(">");
        }
        return arguments;
    }

    private ArgumentPatternNode argument() {

        var required = true;
        var startedWithRequired = false;
        if (iterator.isNext("!")) {
            startedWithRequired = true;
            iterator.next();
        }
        var name = iterator.next();
        var type = getSymbol(":", "Text");
        List<String> suggestions = new ArrayList<String>();
        if (iterator.isNext("[")) {
            suggestions = suggestions();
        }
        if (!startedWithRequired && iterator.isNext("?")) {
            required = false;
        }
        var defaultValue = getSymbol("?", "");
        var properties = new ArrayList<Pair<String, String>>();
        while (iterator.isNext("(")) {
            properties = properties();
        }
        return new ArgumentPatternNode(name, type, required, suggestions, properties, defaultValue);
    }

    private ArrayList<Pair<String, String>> properties() {
        var properties = new ArrayList<Pair<String, String>>();
        iterator.nextOrThrow("(");


        while (!iterator.isNext(")")) {
            properties.add(property());
            if (iterator.isNext(","))
                iterator.next();
        }
        iterator.nextOrThrow(")");
        return properties;
    }

        private List<String> suggestions() {
        var result = new ArrayList<String>();
        iterator.nextOrThrow("[");
        while (iterator.hasNext()) {
            var name = name();
            result.add(name);
            if (iterator.isNext("]")) {
                break;
            }
            iterator.nextOrThrow(",");
        }
        iterator.nextOrThrow("]");
        return result;
    }

    private String name() {
        var name = iterator.next();
        if (iterator.isNext("(")) {
            iterator.nextOrThrow("(");
            iterator.nextOrThrow(")");
        }
        return name;
    }

    private Pair<String, String> property() {


        var name = iterator.next();

        if (iterator.isNext(",") || iterator.isNext(")"))
            return new Pair<>(name, "");

        iterator.nextOrThrow(":");
        var value = name();
        return new Pair<>(name, value);
    }

    private String getSymbol(String symbol, String defaultValue) {
        var result = defaultValue;
        if (iterator.isNext(symbol)) {
            iterator.nextOrThrow(symbol);
            result = iterator.next();
        }
        return result;
    }


}
