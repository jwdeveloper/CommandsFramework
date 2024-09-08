package io.github.jwdeveloper.commands.core.impl.patterns;


import io.github.jwdeveloper.commands.api.exceptions.PatternException;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Data
public class PatternTokenizer implements Iterator<String>, Iterable<String> {

    private List<String> args;
    private int currentIndex = -1;
    private String current = "";
    private String input;

    public PatternTokenizer(String input) {
        this.input = input;
        args = getArgs(input);
    }

    @Override
    public boolean hasNext() {
        return currentIndex < args.size() - 1;
    }

    public boolean isLastToken() {
        return !hasNext();
    }

    public boolean isCurrent(String value) {
        return current().equals(value);
    }

    public String current() {
        return current;
    }

    public String lookup(int offset) {
        if (currentIndex + offset >= args.size()) {
            var index = args.size() - 1;
            if (index < 0)
                return "";

            return args.get(index);
        }
        return args.get(currentIndex + offset);
    }


    public boolean isNext(String value) {
        return lookup(1).equals(value);
    }


    public String lookup() {
        return lookup(1);
    }

    @Override
    public String next() {
        current = lookup(1);
        currentIndex++;
        return current();
    }

    public String nextOrThrow(String required) {
        var nextValue = next();
        if (!nextValue.equals(required)) {

            var msg = "Next token must be: `" + required + "`\nBut was: `" + nextValue + "`\nMake sure the syntax is correct!";
            if (++currentIndex > args.size()) {
                msg = "The last token must be: `" + required + "`\nYou need to append the `" + required + "` at the end";
            }

            throw new PatternException(
                    msg,
                    currentIndex,
                    current,
                    input
            );
        }
        return nextValue;
    }

    public String currentOrThrow(String required) {
        var nextValue = current();
        if (!nextValue.equals(required)) {
            throw new PatternException("Token value should be " + required + " but was " + nextValue,
                    currentIndex,
                    current,
                    input
            );
        }
        return nextValue;
    }

    private List<String> getArgs(String input) {
        var specialSymbols = new HashSet<Character>();
        specialSymbols.add('/');
        specialSymbols.add('(');
        specialSymbols.add(')');
        specialSymbols.add('!');
        specialSymbols.add(':');
        specialSymbols.add('?');
        specialSymbols.add(',');
        specialSymbols.add('[');
        specialSymbols.add(']');
        specialSymbols.add('<');
        specialSymbols.add('>');
        specialSymbols.add('@');
        List<String> result = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            if (current == ' ') {
                continue;
            }
            if (specialSymbols.contains(current)) {
                if (!word.isEmpty()) {
                    result.add(word.toString());
                    word = new StringBuilder();
                }
                result.add(current + "");
                continue;
            }
            word.append(current);
        }
        if (!word.isEmpty()) {
            result.add(word.toString());
        }

        return result;
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }
}