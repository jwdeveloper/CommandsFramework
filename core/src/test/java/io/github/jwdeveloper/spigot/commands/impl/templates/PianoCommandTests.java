package io.github.jwdeveloper.spigot.commands.impl.templates;

import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.spigot.commands.impl.CommandsTestBase;
import io.github.jwdeveloper.spigot.commands.impl.templates.piano.Piano;
import io.github.jwdeveloper.spigot.commands.impl.templates.piano.PianoCommand;
import io.github.jwdeveloper.spigot.commands.impl.templates.piano.PianoService;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PianoCommandTests extends CommandsTestBase {

    @Override
    protected void onBefore(Commands commands) {

        var service = new PianoService();
        service.addPiano("upright");
        service.addPiano("grand");
        service.addPiano("grand-small");

        commands.argumentTypes()
                .create("Piano")
                .onSuggestion(argumentSuggestionEvent -> service.pianos().stream().map(Piano::name).toList())
                .onParseAction(event ->
                {
                    var pianoName = event.nextArgument();
                    var optional = service.findPiano(pianoName);
                    if (optional.isEmpty()) {
                        return ActionResult.failed("Piano has not been found!");
                    }
                    return ActionResult.success(optional.get());
                })
                .register();

        commands.create(new PianoCommand()).register();
    }


    @Test
    public void shouldProperlyRegisterCommand() {
        var optional = api.findByName("piano");
        Assertions.assertTrue(optional.isPresent());
        var piano = optional.get();

        Assertions.assertEquals(1, piano.arguments().size());
        Assertions.assertEquals(2, piano.children().size());
        Assertions.assertEquals("destroy", piano.children().get(0).name());
        Assertions.assertEquals("teleport", piano.children().get(1).name());

    }

    @Test
    public void shouldTriggerPianoCommand() {
        var result = execute("piano", "upright");
        assertTrue(result);
    }

    @Test
    public void shouldTriggerPianoTeleportCommand() {
        var result = execute("piano", "upright", "teleport", "1", "2", "3");
        assertTrue(result);

        var value = result.getValue();
        var x = value.getArgument("x", Double.class);
        var y = value.getArgument("y", Double.class);
        var z = value.getArgument("z", Double.class);

        Assertions.assertEquals(1.0, x);
        Assertions.assertEquals(2.0, y);
        Assertions.assertEquals(3.0, z);
    }
}
