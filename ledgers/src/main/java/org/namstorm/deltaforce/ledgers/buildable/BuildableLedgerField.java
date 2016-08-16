package org.namstorm.deltaforce.ledgers.buildable;

import org.namstorm.deltaforce.core.Buildable;
import org.namstorm.deltaforce.core.BuildableDelta;
import org.namstorm.deltaforce.core.DeltaBuilder;
import org.namstorm.deltaforce.ledgers.LedgerField;

/**
 * Created by maxnam-storm on 16/8/2016.
 */
public class BuildableLedgerField<T extends Buildable> implements LedgerField<T> {
    private BuildableDelta<T> delta;

    public BuildableLedgerField(BuildableDelta<T> delta) {
        this.delta = delta;
    }


    @Override
    public String getFieldName() {
        return delta.getFieldName();
    }

    @Override
    public DeltaBuilder<T> getBuilder() {
        return delta.getBuilder();
    }

    @Override
    public Class getFieldClass() {
        return delta.getOldValue().getClass();
    }

    @Override
    public T getFieldValue() {
        return delta.getNewValue();
    }
}
