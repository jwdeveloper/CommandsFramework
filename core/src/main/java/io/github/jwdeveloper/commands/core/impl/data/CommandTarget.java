package io.github.jwdeveloper.commands.core.impl.data;

import io.github.jwdeveloper.commands.api.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommandTarget {
    private Command command;
    private String[] arguments;
}
