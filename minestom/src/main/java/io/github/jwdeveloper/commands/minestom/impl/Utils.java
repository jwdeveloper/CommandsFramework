package io.github.jwdeveloper.commands.minestom.impl;

public class Utils {

    public static String getCommandName(String input) {

        if (input.startsWith("/")) {
            input = input.substring(1);
        }
        String[] splitArray = input.trim().split("\\s+");
        if (splitArray.length <= 1) {
            return "";
        }
        return splitArray[0];
    }

    public static String[] getArguments(String input) {
        String[] splitArray = input.trim().split("\\s+");
        if (splitArray.length <= 1) {
            return new String[0];
        }
        String[] resultArray = new String[splitArray.length - 1];
        System.arraycopy(splitArray, 1, resultArray, 0, resultArray.length);
        return resultArray;
    }


}
