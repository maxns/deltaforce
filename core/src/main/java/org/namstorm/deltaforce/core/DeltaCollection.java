package org.namstorm.deltaforce.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.namstorm.deltaforce.core.Delta.OP.*;

/**
 * Created by maxnam-storm on 12/8/2016.
 *
 * This is a bit of a funky delta
 */
public class DeltaCollection<T> extends Delta<Collection> implements Collection<Delta<T>> {

    public DeltaCollection(OP op, String fieldName, Collection<T> oldValue) {
        super(op, fieldName, oldValue, new ArrayList<Delta<T>>(oldValue!=null?oldValue.size():10));
    }

    /**
     * applies
     * @param to
     */
    @Override
    public Collection applyTo(final Collection to) {
        Collection res = to!=null?to:new ArrayList();

        for (Delta delta : getNewValue()) {
            switch (delta.getOp()) {
                case ADD:
                    res.add(delta.getNewValue());
                    break;
                case REMOVE:
                    res.remove(delta.getNewValue());
                    break;
                default:
                    throw new UnsupportedOperationException("Don't know how to do:" + delta.getOp() + ", delta:" + delta);
            }
        }
        return res;
    }

    public Collection<Delta<T>> getNewValue() {
        return super.getNewValue();
    }

    public void addDelta(T value) {
        getNewValue().add(new Delta(ADD, "", null, value));
    }

    /**
     *
     * @param value
     * @return wether this was actually in the collection to begin with
     */
    public boolean removeDelta(T value){
        getNewValue().add(new Delta(REMOVE, "", null, value));
        return getOldValue().contains(value);
    }


    @Override
    public int size() {
        return getNewValue().size();
    }

    @Override
    public boolean isEmpty() {
        return getNewValue().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getNewValue().contains(o);
    }

    @Override
    public Iterator<Delta<T>> iterator() {
        return getNewValue().iterator();
    }

    @Override
    public Object[] toArray() {
        return getNewValue().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return getNewValue().toArray(a);
    }

    @Override
    public boolean add(Delta<T> tDelta) {
        return getNewValue().add(tDelta);
    }

    @Override
    public boolean remove(Object o) {
        return getNewValue().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return getNewValue().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Delta<T>> c) {
        return getNewValue().addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return getNewValue().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return getNewValue().retainAll(c);
    }

    @Override
    public void clear() {
        getNewValue().clear();
    }
}
