package org.namstorm.fluency;

import java.util.function.Consumer;

/**
 * Created by maxnam-storm on 12/8/2016.
 *
 * Fluent handing of return values... like magic!
 *
 * @see @link{OnBooleanResult}
 */
public abstract class OnResult<T> {
    public interface ResultConsumer<T> extends Consumer<T> {
    }

    public OnResult(T result) {
        this.result = result;
    }

    T result;
}
