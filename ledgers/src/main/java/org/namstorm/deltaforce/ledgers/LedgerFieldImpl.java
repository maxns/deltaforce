package org.namstorm.deltaforce.ledgers;

import org.namstorm.deltaforce.core.DeltaBuilder;

/**
 * Created by maxnam-storm on 9/8/2016.
 */
public class LedgerFieldImpl<T> implements LedgerField<T> {
    private T value;
    private String fieldName;
    private DeltaBuilder<T> builder;
    private Class fieldClass;

    public LedgerFieldImpl(T value, String fieldName, DeltaBuilder<T> builder, Class fieldClass) {
        this.value = value;
        this.fieldName = fieldName;
        this.builder = builder;
        this.fieldClass = fieldClass;
    }

    @Override
    public String getFieldName() {

        return fieldName;
    }

    @Override
    public DeltaBuilder<T> getBuilder() {
        return builder;
    }

    @Override
    public Class getFieldClass() {
        return fieldClass;
    }

    @Override
    public T getFieldValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setBuilder(DeltaBuilder<T> builder) {
        this.builder = builder;
    }

    public void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }
}
