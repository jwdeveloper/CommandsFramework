package io.github.jwdeveloper.commands.core.impl.parsers;

import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentType;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;

public class TextParser implements ArgumentType {
    @Override
    public String name() {
        return "Text";
    }

    @Override
    public void onArgumentBuilder(ArgumentBuilder builder) {

    }

    @Override
    public ActionResult<Object> onParse(ArgumentParseEvent event) {
        var iterator = event.iterator();
        var current = iterator.next();


        if (current.isEmpty() || current.isBlank()) {
            return ActionResult.failed("value can not be empty");
        }

        var colon = getColon(current);
        if (colon.isEmpty()) {
            return ActionResult.success(current);
        }

        if (checkString(current, colon)) {
            return ActionResult.success(current);
        }


        var builder = new StringBuilder();
        builder.append(current.substring(1));
        while (iterator.hasNext()) {
            current = iterator.next();
            if (current.endsWith(colon)) {
                builder.append(" ").append(current.replace(colon, ""));
                current = colon;
            } else {
                builder.append(" ").append(current);
            }

            if (current.equals(colon))
                break;
        }


        if (!current.endsWith(colon)) {
            return ActionResult.failed("Unmatched quotation marks.");
        }
        return ActionResult.success(builder.toString().stripLeading().stripTrailing());
    }

    public boolean isSingleColon(String current, String colon) {
        if (current.length() == 1 && current.equals(colon)) {
            return true;
        }
        return false;
    }

    public boolean checkString(String current, String colon) {

        if (current.length() == 1) {
            return false;
        }

        char col = colon.charAt(0);
        if (current.charAt(0) == col && current.charAt(current.length() - 1) == col) {
            return true;
        }
        return false;
    }

    public String getColon(String current) {
        if (current.startsWith("\"")) {
            return "\"";
        }
        if (current.startsWith("'")) {
            return "'";
        }
        return "";
    }
}
