package io.github.jwdeveloper.commands.core.impl.data;

import lombok.Data;

@Data
public class Ref<T> {

    T value;

    public Ref(T value) {
        this.value = value;
    }

    public Ref() {
        value = null;
    }
}
