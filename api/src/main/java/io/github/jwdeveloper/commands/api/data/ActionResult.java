package io.github.jwdeveloper.commands.api.data;

import lombok.Data;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Represents the result of an action with optional success status and message.
 *
 * @param <T> the type of the result object
 */
@Data
public class ActionResult<T> {
    private boolean success = true;
    private T value;
    private String message;

    /**
     * Default constructor
     */
    public ActionResult() {
    }

    /**
     * @param object the default value of action result
     */
    protected ActionResult(T object) {
        this.value = object;
    }

    /**
     * @param object the default value of action result.
     * @param status the initial state
     */
    protected ActionResult(T object, boolean status) {
        this(object);
        this.success = status;
    }

    /**
     * @param object  the default value of action result.
     * @param status  the initial state
     * @param message the message of action result
     */
    protected ActionResult(T object, boolean status, String message) {
        this(object, status);
        this.message = message;
    }

    /**
     * @param message exception message
     * @return returns value, if value is empty then throws exception with provided message
     */
    public T getOrThrow(String message) {
        if (value == null) {
            throw new RuntimeException(message);
        }
        return value;
    }

    /**
     * Checks if the action has failed.
     *
     * @return {@code true} if the action is unsuccessful; {@code false} otherwise.
     */
    public boolean isFailed() {
        return !isSuccess();
    }

    /**
     * Checks if a message is associated with the action result.
     *
     * @return {@code true} if there is a message; {@code false} otherwise.
     */
    public boolean hasMessage() {
        return message != null;
    }

    /**
     * Checks if an object is associated with the action result.
     *
     * @return {@code true} if there is an object; {@code false} otherwise.
     */
    public boolean hasObject() {
        return value != null;
    }

    /**
     * Creates a successful {@link ActionResult} with no associated value.
     *
     * @param <T> the type of the action result's value
     * @return a successful action result instance.
     */
    public static <T> ActionResult<T> success() {
        return new ActionResult<>(null, true);
    }

    /**
     * Casts the current {@link ActionResult} to a new type with the specified value.
     *
     * @param <Output> the type of the new action result's value
     * @param output   the new value to associate with the action result
     * @return a new action result with the specified value, retaining the current success state and message.
     */
    public <Output> ActionResult<Output> cast(Output output) {
        return new ActionResult<>(output, this.isSuccess(), this.getMessage());
    }

    /**
     * Casts the current {@link ActionResult} to a new type without changing the value.
     *
     * @param <Output> the type of the new action result's value
     * @return a new action result with a null value, retaining the current success state and message.
     */
    public <Output> ActionResult<Output> cast() {
        return new ActionResult<>(null, this.isSuccess(), this.getMessage());
    }

    /**
     * Casts the current {@link ActionResult} to a new type using a converter function.
     *
     * @param <Output>  the type of the new action result's value
     * @param converter a function to convert the current value to the new type
     * @return a new action result with the converted value, retaining the current success state and message.
     */
    public <Output> ActionResult<Output> cast(Function<T, Output> converter) {
        var value = this.value != null ? converter.apply(this.value) : null;
        return new ActionResult<>(value, this.isSuccess(), this.getMessage());
    }

    /**
     * Casts an existing {@link ActionResult} to a new type with a specified value.
     *
     * @param <Input>  the type of the original action result's value
     * @param <Output> the type of the new action result's value
     * @param action   the original action result to cast
     * @param output   the new value to associate with the casted action result
     * @return a new action result with the specified value, retaining the success state and message from the original result.
     */
    public static <Input, Output> ActionResult<Output> cast(ActionResult<Input> action, Output output) {
        return new ActionResult<>(output, action.isSuccess(), action.getMessage());
    }

    /**
     * Creates a successful {@link ActionResult} with the specified value.
     *
     * @param <T>     the type of the action result's value
     * @param payload the value associated with the successful action result
     * @return a successful action result with the specified value.
     */
    public static <T> ActionResult<T> success(T payload) {
        return new ActionResult<>(payload, true);
    }

    /**
     * Creates a successful {@link ActionResult} with the specified value and message.
     *
     * @param <T>     the type of the action result's value
     * @param payload the value associated with the successful action result
     * @param message the message to associate with the successful action result
     * @return a successful action result with the specified value and message.
     */
    public static <T> ActionResult<T> success(T payload, String message) {
        return new ActionResult<>(payload, true, message);
    }

    /**
     * Creates a failed {@link ActionResult} with no associated value.
     *
     * @param <T> the type of the action result's value
     * @return a failed action result with no value.
     */
    public static <T> ActionResult<T> failed() {
        return new ActionResult<>(null, false);
    }

    /**
     * Creates a failed {@link ActionResult} with a specified message.
     *
     * @param <T>     the type of the action result's value
     * @param message the message to associate with the failed action result
     * @return a failed action result with the specified message.
     */
    public static <T> ActionResult<T> failed(String message) {
        return new ActionResult<>(null, false, message);
    }

    /**
     * Creates a failed {@link ActionResult} with a specified value and message.
     *
     * @param <T>     the type of the action result's value
     * @param target  the value associated with the failed action result
     * @param message the message to associate with the failed action result
     * @return a failed action result with the specified value and message.
     */
    public static <T> ActionResult<T> failed(T target, String message) {
        return new ActionResult<>(target, false, message);
    }

    /**
     * Creates an {@link ActionResult} from an {@link Optional}.
     *
     * @param <T>      the type of the action result's value
     * @param optional the optional value to create the action result from
     * @return a successful action result if the optional contains a value, or a failed action result otherwise.
     */
    public static <T> ActionResult<T> fromOptional(Optional<T> optional) {
        return optional.map(ActionResult::success).orElseGet(ActionResult::failed);
    }


    public void ifPresent(Consumer<T> action) {
        if (isSuccess()) {
            action.accept(value);
        }
    }

}
