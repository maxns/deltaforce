package org.namstorm.deltaforce.core;

/**
 * Created by maxns on 5/8/2016.
 */
public class Delta<T> {
    public enum OP {
        ADD,
        REMOVE,
        UPDATE,
        NOOP
    }

    String fieldName;
    OP op;
    T newValue;
    T oldValue;

    public T getOldValue() {
        return oldValue;
    }


    public Delta(OP op, String fieldName, T oldValue, T newValue) {
        this.op = op;
        this.fieldName = fieldName;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public OP getOp() {
        return op;
    }

    public T getNewValue() {
        return newValue;
    }
}
