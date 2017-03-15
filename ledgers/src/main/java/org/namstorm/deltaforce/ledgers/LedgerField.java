package org.namstorm.deltaforce.ledgers;


import org.namstorm.deltaforce.core.DeltaBuilder;

/**
 * Created by maxnam-storm on 9/8/2016.
 */
public interface LedgerField<T> {
    String getFieldName();
    DeltaBuilder<T> getBuilder();
    Class getFieldClass();
    T getFieldValue();
}
