package io.github.jwdeveloper.commands.api.exceptions;

/**
 * Invoked when there was an error during the pattern parsing
 */
public class PatternException extends RuntimeException {

    /**
     * @param message exception message
     */
    public PatternException(String message, int index, String current, String input) {
        super(formatMessage(message, index, current, input));
    }

    /**
     * @param message exception message
     * @param cause   the cause
     */
    public PatternException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause the cause
     */
    public PatternException(Throwable cause) {
        super(cause);
    }

    /**
     * Impossible to parse command pattern because of the syntax error:
     * You forgot to insert the > symbol ;)
     * <p>
     * ===================
     * \/
     * /test <arg1:Text
     *
     * @param message
     * @param index
     * @param current
     * @param input
     * @return
     */
    public static String formatMessage(String message, int index, String current, String input) {
        var sb = new StringBuilder();
        sb.append("\n");
        sb.append("Impossible to parse command pattern because of the syntax error: \n");
        sb.append(message).append(" \n");
        sb.append(input);

        return sb.toString();
    }
}

