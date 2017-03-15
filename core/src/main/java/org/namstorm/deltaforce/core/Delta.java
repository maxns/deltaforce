package org.namstorm.deltaforce.core;

import java.io.Serializable;

/**
 * Created by maxns on 5/8/2016.
 */
public class Delta<T> implements Serializable{

    private static final long serialVersionUID = -8507588398853728545L;

    public enum OP {
        ADD,
        REMOVE,
        UPDATE,
        NOOP
    }

    private final String fieldName;

    public void setOp(OP op) {
        this.op = op;
    }

    private OP op;

    public void setNewValue(T newValue) {
        this.newValue = newValue;
    }

    private T newValue;

    public void setOldValue(T oldValue) {
        this.oldValue = oldValue;
    }

    private T oldValue;

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

    public T applyTo(T to) {
        to = this.getNewValue();
        return to;
    }
}
