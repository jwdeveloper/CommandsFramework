package io.github.jwdeveloper.commands.core.impl.data;

import io.github.jwdeveloper.dependance.injector.api.util.Pair;

import java.util.ArrayList;
import java.util.List;

public record ArgumentPatternNode(String name,
                                  String type,
                                  boolean required,
                                  List<String> suggestions,
                                  ArrayList<Pair<String, String>> properties,
                                  String defaultValue) {

        public boolean hasProperty(String property) {
            return properties.stream().anyMatch(e -> e.getKey().equals(property));
        }

        public String getProperty(String property) {
            return properties.stream().filter(e -> e.getKey().equals(property)).findFirst().get().getValue();
        }
    }