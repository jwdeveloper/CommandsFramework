package io.github.jwdeveloper.commands.api.data.events;

import io.github.jwdeveloper.commands.api.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;


@AllArgsConstructor
@Data
@Accessors(fluent = true)
public class CommandErrorEvent<SENDER_TYPE> {
    private SENDER_TYPE sender;
    private Command command;
    private Exception exception;
    private String[] args;
    private String errorMessage;
}
