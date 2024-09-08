package io.github.jwdeveloper.commands.api.iterators;


import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
@Data
public class ArgumentIterator implements Iterator<String>, Iterable<String> {

    private List<String> args;
    private int currentIndex = -1;
    private String current = "";


    /**
     * @param input the list of input parameters
     */
    public ArgumentIterator(String[] input) {
        args = new ArrayList<>(Arrays.stream(input).toList());
    }

    /**
     * @return true if there is more tokens
     */
    @Override
    public boolean hasNext() {

        return currentIndex < args.size() - 1;
    }

    /**
     * @return true if current value is the last token
     */
    public boolean isLastToken() {
        return currentIndex == args.size() - 1;
    }

    /**
     * @param expectedValue the excepted value
     * @return true if current value is equal excepted value
     */
    public boolean isCurrent(String expectedValue) {
        return current().equals(expectedValue);
    }

    /**
     * @return the current value of iterator
     */
    public String current() {
        return current;
    }

    /**
     * @param offset the offset relative to current index
     * @return the value of currentIndex+offset
     */
    public String lookup(int offset) {

        if (args.isEmpty()) {
            return "";
        }

        if (currentIndex + offset >= args.size()) {
            return args.get(args.size() - 1);
        }
        return args.get(currentIndex + offset);
    }

    /**
     * Replace the token value at the specific index
     * @param offset offset relative to current index
     * @param value the value
     */
    public void replace(int offset, String value) {
        if (args.isEmpty()) {
            return;
        }

        if (currentIndex + offset >= args.size()) {
            args.set(args.size() - 1, value);
        }
        args.set(currentIndex + offset, value);
    }


    /**
     * @param expectedValue the expected value
     * @return true is next value is equal to expected value
     */
    public boolean isLookup(String expectedValue) {
        return lookup(1).equals(expectedValue);
    }


    /**
     * @return the next token value
     */
    public String lookup() {
        return lookup(1);
    }

    /**
     * Iterate to the next token
     *
     * @return the next token value
     */
    @Override
    public String next() {
        current = lookup(1);
        currentIndex++;
        return current();
    }

    /**
     * If next value is not equal to the expected value then
     * exception is thrown
     *
     * @param expected the expected value
     * @return the next token value
     */
    public String nextOrThrow(String expected) {
        var nextValue = next();
        if (!nextValue.equals(expected)) {
            throw new RuntimeException("Next Token should be " + expected + " but was " + nextValue);
        }
        return nextValue;
    }

    /**
     * @param required if the current value is different from
     *                 the required parameter, then exception is thrown
     * @return the current value
     */
    public String currentOrThrow(String required) {
        var nextValue = current();
        if (!nextValue.equals(required)) {
            throw new RuntimeException("Next Token should be " + required + " but was " + nextValue);
        }
        return nextValue;
    }


    private List<String> getArgs(String input) {
        int counter = 1;
        StringBuilder currentToken = new StringBuilder();
        int curlyBraceCount = 0;
        char stringDelimiter = 0;

        boolean commandMode = false;


        var specialSymbols = new ArrayList<Character>();

        specialSymbols.add('(');
        specialSymbols.add(')');
        specialSymbols.add(',');
        specialSymbols.add('+');
        specialSymbols.add('[');
        specialSymbols.add(']');
        List<String> result = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);

            if (current == '\n') {
                if (commandMode) {
                    result.add(currentToken.toString());
                    counter++;
                    currentToken.setLength(0);
                    commandMode = false;
                }
                continue;
            }

            if (commandMode) {
                currentToken.append(current);
                continue;
            }

            if (curlyBraceCount == 0 && stringDelimiter == 0 && current == '/') {
                commandMode = true;
            }

            if (specialSymbols.contains(current)) {
                var value = currentToken.toString();
                if (!value.equals("")) {
                    result.add(currentToken.toString());
                }
                result.add(current + "");
                counter++;
                currentToken.setLength(0);
                continue;
            }

           /*if (current == '(' ) {
                var value = currentToken.toString();
                if(!value.equals(""))
                {
                    result.add(currentToken.toString());
                }
                result.add("(");
                counter++;
                currentToken.setLength(0);
                continue;
            }


            if (current == '+' ) {
                var value = currentToken.toString();
                if(!value.equals(""))
                {
                    result.add(currentToken.toString());
                }
                result.add("+");
                counter++;
                currentToken.setLength(0);
                continue;
            }

            if (current == ',' ) {
                //System.out.println(counter + " " + currentToken);

                var value = currentToken.toString();
                if(!value.equals(""))
                {
                    result.add(currentToken.toString());
                }
                result.add(",");
                counter++;
                currentToken.setLength(0);
                continue;
            }

            if (current == ')' ) {
                var value = currentToken.toString();
                if(!value.equals(""))
                {
                    result.add(currentToken.toString());
                }
                result.add(")");
                counter++;
                currentToken.setLength(0);
                continue;
            }*/

            if (stringDelimiter != 0) {
                // We're inside a string
                currentToken.append(current);
                if (current == stringDelimiter) {
                    // End of string
                    //  System.out.println(counter + " " + currentToken);
                    result.add(currentToken.toString());
                    counter++;
                    currentToken.setLength(0);
                    stringDelimiter = 0;
                }
                continue;
            }

            if (current == '"' || current == '\'') {
                // Start of string
                stringDelimiter = current;
                currentToken.append(current);
                continue;
            }

            if (current == ' ' && curlyBraceCount == 0 && currentToken.length() == 0) {
                continue;
            }

            if (current == ' ' && curlyBraceCount == 0) {
                //System.out.println(counter + " " + currentToken);
                result.add(currentToken.toString());
                counter++;
                currentToken.setLength(0);
                continue;
            }


            if (current == '{') {
                curlyBraceCount++;
            }

            if (current == '}') {
                curlyBraceCount--;
            }

            currentToken.append(current);


            if (curlyBraceCount == 0 && current == '}') {
                //System.out.println(counter + " " + currentToken);
                result.add(currentToken.toString());
                counter++;
                currentToken.setLength(0);
            }
        }

        if (currentToken.length() > 0) {
            // System.out.println(counter + " " + currentToken);
            result.add(currentToken.toString());
        }
        return result;
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    /**
     * appends new value to iterator
     *
     * @param value new value
     */
    public void append(String value) {
        args.add(value);
    }
}