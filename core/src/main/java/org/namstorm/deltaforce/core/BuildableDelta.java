package org.namstorm.deltaforce.core;

/**
 * Created by maxnamstorm on 16/8/2016.
 */
public class BuildableDelta<T> extends Delta<T> {

    public BuildableDelta(OP op, String fieldName, T oldValue, T newValue, DeltaBuilder<T> builder) {
        super(op, fieldName, oldValue, newValue);
        this.builder = builder;
    }

    public DeltaBuilder<T> getBuilder() {
        return builder;
    }

    private DeltaBuilder<T> builder;

    @Override
    public T applyTo(T to) {
        return builder.from(to!=null?to:newValue).apply();
    }
}
