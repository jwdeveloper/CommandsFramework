package io.github.jwdeveloper.spigot.commands.impl.services;

import io.github.jwdeveloper.commands.api.data.events.CommandEventImpl;
import io.github.jwdeveloper.commands.api.data.expressions.CommandNode;
import io.github.jwdeveloper.commands.core.impl.services.CommandEventsImpl;
import io.github.jwdeveloper.commands.api.Commands;
import io.github.jwdeveloper.spigot.commands.impl.common.CommandsTestBase;
import io.github.jwdeveloper.spigot.commands.impl.common.SenderType1;
import io.github.jwdeveloper.spigot.commands.impl.common.SenderType1SubType;
import io.github.jwdeveloper.spigot.commands.impl.common.SenderType2;
import io.github.jwdeveloper.spigot.commands.impl.common.SenderType3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EventsServiceTest extends CommandsTestBase {
    private CommandEventsImpl sut;

    @Override
    protected void onBefore(Commands commands) {
        sut = new CommandEventsImpl();
    }

    @Test
    public void shouldInvokeOnExecuteForAnySender() {

        var sender1 = new SenderType1();
        var sender2 = new SenderType2();
        var sender3 = new SenderType3();

        AtomicInteger invocationCounter = new AtomicInteger();
        sut.onCommandInvoked(Object.class, event ->
        {
            invocationCounter.incrementAndGet();
        });

        sut.publishCommandInvoked(new CommandEventImpl<>(sender1, List.of(new CommandNode())));
        sut.publishCommandInvoked(new CommandEventImpl<>(sender2, List.of(new CommandNode())));
        sut.publishCommandInvoked(new CommandEventImpl<>(sender3, List.of(new CommandNode())));

        Assertions.assertEquals(3, invocationCounter.get());
    }


    @Test
    public void shouldInvokeOnlyForGivenType() {

        var sender1 = new SenderType1();
        var sender2 = new SenderType2();
        var sender3 = new SenderType3();

        AtomicInteger invocationCounter = new AtomicInteger();
        sut.onCommandInvoked(SenderType1.class, event ->
        {
            invocationCounter.incrementAndGet();
        });

        sut.publishCommandInvoked(new CommandEventImpl<>(sender1, List.of(new CommandNode())));
        sut.publishCommandInvoked(new CommandEventImpl<>(sender2, List.of(new CommandNode())));
        sut.publishCommandInvoked(new CommandEventImpl<>(sender3, List.of(new CommandNode())));

        Assertions.assertEquals(1, invocationCounter.get());
    }


    @Test
    public void shouldInvokeOnlyForTypeThatIsAssignable() {

        var sender1 = new SenderType1SubType();
        AtomicInteger invocationCounter = new AtomicInteger();
        sut.onCommandInvoked(SenderType1.class, event ->
        {
            invocationCounter.incrementAndGet();
        });

        sut.publishCommandInvoked(new CommandEventImpl<>(sender1, List.of(new CommandNode())));
        Assertions.assertEquals(1, invocationCounter.get());
    }
}
