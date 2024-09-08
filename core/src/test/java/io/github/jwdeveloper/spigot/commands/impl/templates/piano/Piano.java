package io.github.jwdeveloper.spigot.commands.impl.templates.piano;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Piano {
    private String name;
    private String id;

}
