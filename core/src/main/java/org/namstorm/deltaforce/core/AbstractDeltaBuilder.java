package org.namstorm.deltaforce.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxns on 5/8/2016.
 * <p>
 * This is the base class for DeltaBuilders that get generated
 *
 * @see @DeltaBuilder
 */
public abstract class AbstractDeltaBuilder<T extends Object> {

    private T result;

    private Map<String, Delta<?>> deltaMap = new HashMap<String, Delta<?>>();

    public AbstractDeltaBuilder(T from) {
        super();
        from(from);
    }

    protected void addDelta(Delta<?> delta) {
        deltaMap.put(delta.getFieldName(), delta);
    }

    public AbstractDeltaBuilder<T> from(T from) {
        this.result = from;
        return this;
    }

    public void visitDeltas(DeltaVisitor visitor) {
        for (Delta<?> delta : deltaMap.values()) {
            visitor.visit(delta.getOp(), delta.getFieldName(), delta.getNewValue());
        }
    }

    protected T result() {
        return result;
    }

    protected Map<String, Delta<?>> deltaMap() {
        return deltaMap;
    }


    public T build() {
        return apply(result());
    }

    /**
     * This will be generated
     *
     * @param to
     * @return result
     */
    protected abstract T apply(T to);


    /**
     * does comparison of objects
     *
     * @param oldval
     * @param newval
     * @return Delta.OP value of
     * <p>
     * NOOP - if objects are the same (even if null)
     * ADD - if old value was null (and new is not)
     * REMOVE - if old value was NOT null (and new is)
     * UPDATE - if neither was null but are not equal
     */
    protected Delta.OP compare(Object oldval, Object newval) {
        if (oldval == newval) {
            return Delta.OP.NOOP;
        } else if (oldval == null/* above already implies newval !=null */) {
            return Delta.OP.ADD;
        } else if (newval == null/* above already imples oldval !=null*/) {
            return Delta.OP.REMOVE;
        } else if (newval.equals(oldval)) {
            return Delta.OP.NOOP;
        } else {
            return Delta.OP.UPDATE;
        }
    }


}
