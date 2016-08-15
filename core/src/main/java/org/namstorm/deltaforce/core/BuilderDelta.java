package org.namstorm.deltaforce.core;

/**
 * Created by maxnamstorm on 16/8/2016.
 */
public class BuilderDelta<T> extends Delta<T> {

    public BuilderDelta(OP op, String fieldName, T oldValue, T newValue, DeltaBuilder<T> builder) {
        super(op, fieldName, oldValue, newValue);
        this.builder = builder;
    }

    public DeltaBuilder<T> getBuilder() {
        return builder;
    }

    private DeltaBuilder<T> builder;

}
