package io.github.jwdeveloper.commands.core.impl.data;

import io.github.jwdeveloper.commands.core.impl.patterns.PatternParser;
import io.github.jwdeveloper.dependance.injector.api.util.Pair;

import java.util.ArrayList;
import java.util.List;


public record CommandNode(String name,
                          ArrayList<Pair<String, String>> properties,
                          List<String> namesChain,
                          List<ArgumentNode> arguments) {
}
