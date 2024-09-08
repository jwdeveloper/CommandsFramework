package io.github.jwdeveloper.commands.api.data.events;

import io.github.jwdeveloper.commands.api.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;


@AllArgsConstructor
@Data
@Accessors(fluent = true)
public class CommandValidationEvent<SENDER> {
    private Command command;
    private SENDER sender;
    private String[] args;

}
