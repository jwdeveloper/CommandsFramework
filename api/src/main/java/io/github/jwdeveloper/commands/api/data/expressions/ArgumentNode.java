package io.github.jwdeveloper.commands.api.data.expressions;

import io.github.jwdeveloper.commands.api.argumetns.ArgumentProperties;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ArgumentNode {
    private ArgumentProperties argument;
    private Object value;
    private boolean isEnd;
    private boolean valid;
    private String validationMessage;
}