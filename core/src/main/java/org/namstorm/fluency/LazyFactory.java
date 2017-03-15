package org.namstorm.fluency;

import java.util.function.Function;

/**
 * Created by maxnamstorm on 14/8/2016.
 */
public class LazyFactory<T> extends OnResult<T> {

    public static <C,P> C create(C test, P param, Function<P, C> factory) {
        if(test==null) {
            return factory.apply(param);
        }else {
            return test;
        }
    }

    public abstract class Factory<T> {
        public abstract T create();
    }
    public LazyFactory(T result) {
        super(result);
    }

    public T create(Factory<T> f) {
        if(this.result == null) {
            result = f.create();
        }
        return result;
    }
}
