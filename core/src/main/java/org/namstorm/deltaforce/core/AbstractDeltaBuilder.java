package org.namstorm.deltaforce.core;

import java.util.Map;

/**
 * Created by maxns on 5/8/2016.
 * <p>
 * This is the base class for DeltaBuilders that get generated
 *
 * @see @DeltaBuilder
 */
public abstract class AbstractDeltaBuilder<T extends Object> {

    private T from;

    private DeltaMap deltaMap;

    public AbstractDeltaBuilder() {
        super();

    }

    /**
     * initialises the from
     *
     * @param from
     */
    protected AbstractDeltaBuilder(T from) {
        super();

        from(from);
    }

    /**
     * All deltas from now on will be vs this object
     *
     * @param from
     * @return
     */
    public AbstractDeltaBuilder<T> from(T from) {
        this.from = from;
        return this;
    }

    protected T _from() {
        return from == null? (from=create()):from;
    }

    /**
     * Adds a delta
     * @param delta
     */
    protected void addDelta(Delta<?> delta) {
        deltaMap.addDelta(delta.getFieldName(), delta);
    }

    /**
     * adds a delta within a map
     *
     * @param mapFieldName
     * @param curMap
     * @param fieldDelta
     */
    protected void addMapDelta(String mapFieldName, Map curMap, Delta<?> fieldDelta) {
        DeltaMap map = (DeltaMap) deltaMap().map().get(mapFieldName);

        if(map==null) {
            map = new DeltaMap(mapFieldName, curMap);
            addDelta(map);
        }
        map.addDelta(fieldDelta.getFieldName(), fieldDelta);
    }

    /**
     * applies delta to map
     *
     * @param from
     * @param to
     */
    protected void applyToMap(DeltaMap from, Map to) {
        DeltaUtil.applyMapDeltas(from, to);
    }

    /**
     * Visit all deltas
     * @see DeltaVisitor
     * @param visitor
     */
    public void visitDeltas(DeltaVisitor visitor) {
        DeltaUtil.visitDeltas(deltaMap(), visitor);
    }

    protected T result() {
        return from;
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


    /**
     * Applies collected deltas to a new object (which gets created on the spot)
     *
     * @see .apply(T)
     * @see .create()
     *
     * @return
     */
    public T build() {
        return apply(create());
    }

    /**
     * applies deltas to an object to
     *
     * @param to object to which to apply this
     * @return from
     */
    public abstract T apply(T to);

    /**
     * creates a new object
     *
     * @see T .build()
     * @see T .apply(T)
     *
     * @return
     */
    public abstract T create();

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
