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
    public void setBuilder(DeltaBuilder<T> builder) {
        this.builder = builder;
    }

    private DeltaBuilder<T> builder;


    @Override
    public T applyTo(T to) {
        switch(getOp()) {
            case ADD:
            case UPDATE:
                return to!=null
                        ?builder.from(to).apply()
                        :builder.apply();

            case REMOVE:
                return null;
            case NOOP:
                return getOldValue();


        }
        throw new UnsupportedOperationException("Unknown OP:" + getOp());
    }
}
