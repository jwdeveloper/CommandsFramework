package io.github.jwdeveloper.spigot.commands.impl;

import io.github.jwdeveloper.commands.core.impl.data.Ref;
import io.github.jwdeveloper.spigot.commands.impl.CommandsTestBase;

import io.github.jwdeveloper.commands.api.data.ActionResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GlobalEventsTests extends CommandsTestBase {


   /* @Test
    public void should_use_global_error_event() {

        var globalValidation = new Ref<>(false);
        var localValiudation = new Ref<>(false);
        api.actions().onError(event ->
        {
            globalValidation.setValue(true);
        });
        create("/test")
                .onError(event ->
                {
                    localValiudation.setValue(true);
                })
                .onExecute(event ->
                {
                    var i = 0;
                    i = i / 0;
                })
                .register();
        var result = execute("test");
        assertFalse(result);
        Assertions.assertEquals(true, localValiudation.getValue());
        Assertions.assertEquals(true, globalValidation.getValue());
    }


    @Test
    public void should_use_global_finish_event() {

        var globalValidation = new Ref<>(false);
        var localValiudation = new Ref<>(false);
        api.actions().onFinalize(event ->
        {
            globalValidation.setValue(true);
        });
        create("/test").onFinished(event ->
        {
            localValiudation.setValue(true);
        }).register();
        var result = execute("test");
        assertTrue(result);
        Assertions.assertEquals(true, localValiudation.getValue());
        Assertions.assertEquals(true, globalValidation.getValue());
    }

  //  @Test
    public void should_use_global_validation_event() {
        var globalValidation = new Ref<>(false);
        var localValiudation = new Ref<>(false);
        api.actions().onValidation(event ->
        {
            globalValidation.setValue(true);
            return true;
        });
        create("/test").onValidation(event ->
        {
            localValiudation.setValue(true);
            return ActionResult.success();
        }).register();
        var result = execute("test");
        assertTrue(result);
        Assertions.assertEquals(true, localValiudation.getValue());
        Assertions.assertEquals(true, globalValidation.getValue());
    }*/
}
