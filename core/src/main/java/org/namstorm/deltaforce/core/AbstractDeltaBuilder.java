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

    private DeltaMap deltaMap;

    public AbstractDeltaBuilder() {
        super();

    }
    public AbstractDeltaBuilder(T from) {
        super();

        from(from);
    }
    public AbstractDeltaBuilder<T> from(T from) {
        this.result = from;
        return this;
    }

    protected void addDelta(Delta<?> delta) {
        deltaMap.addDelta(delta.getFieldName(), delta);
    }

    protected void addMapDelta(String mapFieldName, Map curMap, Delta<?> fieldDelta) {
        DeltaMap map = (DeltaMap) deltaMap().map().get(mapFieldName);

        if(map==null) {
            map = new DeltaMap(mapFieldName, curMap);
            addDelta(map);
        }
        map.addDelta(fieldDelta.getFieldName(), fieldDelta);
    }

    protected void applyToMap(DeltaMap from, Map to) {
        DeltaUtil.applyMapDeltas(from, to);
    }

    public void visitDeltas(DeltaVisitor visitor) {
        DeltaUtil.visitDeltas(deltaMap(), visitor);
    }

    protected T result() {
        return result;
    }

    /**
     * by default this is just a getter with lazy construction
     * @return
     */
    protected DeltaMap deltaMap() {
        return deltaMap == null ? deltaMap = createDeltaMap() : deltaMap;
    }

    /**
     * creates a DeltaMap, but since this is a root element, it has the fieldName set to current class name
     * @return
     */
    protected DeltaMap createDeltaMap() {
        return new DeltaMap(this.getClass().toString(),null);
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
