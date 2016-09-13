package org.namstorm.deltaforce.core;

import java.util.Collection;
import java.util.Map;

import org.namstorm.fluency.OnBooleanResult;

/**
 * Created by maxns on 5/8/2016.
 * <p>
 * This is the base class for DeltaBuilders that get generated
 *
 * @see @DeltaBuilder
 */
public abstract class AbstractDeltaBuilder<T> implements DeltaBuilder<T> {

    private T from;

    private DeltaMap<String,Delta<?>> deltaMap;

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
     * This also means that when you execute update() it will update this object and return
     *
     * @param from
     * @return
     */
    public DeltaBuilder<T> from(T from) {
        this.from = initBuilder(from);
        return this;
    }



    /**
     * bit of a hack for now
     * @param buildable
     * @return
     */
    protected T initBuilder(T buildable) {
        if(buildable instanceof Buildable) {
            ((Buildable) buildable).setBuilder(this);
        }
        return buildable;
    }



    public T from() {
        return from;
    }

    /**
     * Adds a delta
     * @param delta
     */
    protected void addDelta(Delta<?> delta) {
        deltaMap.addDelta(delta.getFieldName(), delta);
    }

    protected <TBuildable extends Buildable> void addDelta(BuildableDelta<TBuildable> delta, DeltaBuilder<TBuildable> builder) {
        addDelta(delta);

        delta.setBuilder(builder);
    }

    /**
     * adds a delta within a map
     *
     * @param mapFieldName
     * @param curMap
     * @param fieldDelta
     */
    protected void addMapDelta(String mapFieldName, Map curMap, Delta<?> fieldDelta) {
        DeltaMap<String,Delta<?>> map = (DeltaMap<String, Delta<?>>) deltaMap().map().get(mapFieldName);

        if(map==null) {
            map = new DeltaMap<>(Delta.OP.UPDATE, mapFieldName, curMap);
            addDelta(map);
        }
        map.addDelta(fieldDelta.getFieldName(), fieldDelta);
    }

    protected <C> void addToCollection(String colFieldName, Collection<C> curCol, C valueToAdd) {

        getCollectionDelta(colFieldName, curCol).addDelta(valueToAdd);
    }

    protected <O, B extends DeltaBuilder<O>> BuildableDelta<O> createBuildableDelta(String fieldName, O curValue, O newValue, B builder) {
        return new BuildableDelta<>(compare(curValue, newValue), fieldName, curValue, newValue, builder.from(curValue));

    }



    /**
     *
     * @param colFieldName
     * @param curCol
     * @param removeValue
     * @param <C>
     * @return OnBooleanResult - true if object existed in the original collection
     *
     * @link{OnBooleanResult}
     */
    protected <C> OnBooleanResult removeFromCollection(String colFieldName, Collection<C> curCol, C removeValue) {

        return OnBooleanResult.cast(getCollectionDelta(colFieldName, curCol).removeDelta(removeValue));
    }


    private <C> DeltaCollection<C> getCollectionDelta(String colFieldName, Collection<C> curCol) {
        // oh yeah, delta of collection of deltas of C...
        DeltaCollection<C> dc = (DeltaCollection<C>) deltaMap().get(colFieldName);

        if(dc == null) {
            dc = new DeltaCollection<>(Delta.OP.UPDATE, colFieldName, curCol);
            addDelta(dc); // store the delta of the collection (not the item)
        }

        return dc;
    }

    protected Delta delta(String field) {
        return deltaMap().get(field);
    }

    /**
     * Visit all deltas
     * @see DeltaVisitor
     * @param visitor
     */
    @Override
    public void visitDeltas(DeltaVisitor visitor) {
        DeltaUtil.visitDeltas(deltaMap(), visitor);
    }


    /**
     * by default this is just a getter with lazy construction
     * @return
     */
    protected DeltaMap<String, Delta<?>> deltaMap() {
        return deltaMap == null ? deltaMap = createDeltaMap() : deltaMap;
    }

    /**
     * creates a DeltaMap, but since this is a root element, it has the fieldName set to current class name
     * @return
     */
    protected DeltaMap<String, Delta<?>> createDeltaMap() {
        return new DeltaMap<>(Delta.OP.UPDATE, this.getClass().toString(),null);
    }

    public DeltaMap<String, Delta<?>> getDeltaMap() {
        return deltaMap;
    }

    public void setDeltaMap(DeltaMap<String, Delta<?>> map) {
        deltaMap = map;
    }

    protected boolean containsDelta(String field) {
        return deltaMap().containsKey(field);
    }

    /**
     * Applies collected deltas to a new object (which gets created on the spot)
     *
     * @see .apply(T)
     * @see .create()
     *
     * @return
     */
    @Override
    public T build() {
        return from(create()).apply();
    }

    @Override
    public DeltaBuilder op(Delta.OP op) {
        deltaMap().setOp(op);
        return this;
    }

    /**
     * does comparison of objects
     *
     * @param oldval - old value
     * @param newval - new value
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
