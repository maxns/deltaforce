package org.namstorm.deltaforce.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public class CollectionDeltaBuilder<T> extends AbstractDeltaBuilder<Collection<T>> {


    @Override
    public Collection<T> apply(Collection<T> to) {

        to.addAll( (Collection<T>) deltaMap().get(this).getNewValue() );
        return to;
    }

    @Override
    public Collection<T> create() {
        return createCollection();
    }

    private Collection<T> createCollection() {
        return new ArrayList<>();
    }


}
