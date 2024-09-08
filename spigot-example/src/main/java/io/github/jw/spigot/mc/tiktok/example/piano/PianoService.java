package io.github.jw.spigot.mc.tiktok.example.piano;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PianoService {


    private final List<Piano> pianos = new ArrayList<>();

    public  Optional<Piano> findPiano(String name) {
        return pianos.stream().filter(e -> e.name().equals(name)).findFirst();
    }


    public List<Piano> pianos() {
        return pianos;
    }

    public void addPiano(String two) {
        var piano = new Piano();
        piano.name(two);
        piano.id(UUID.randomUUID().toString());
        pianos.add(piano);
        return;
    }
}
