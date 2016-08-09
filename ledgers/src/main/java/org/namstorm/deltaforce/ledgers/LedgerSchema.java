package org.namstorm.deltaforce.ledgers;

import org.namstorm.deltaforce.annotations.DeltaBuilder;

import java.util.Set;

/**
 * Created by maxnam-storm on 9/8/2016.
 *
 */
public interface LedgerSchema<T extends LedgerField> {

    Set<T> fields();
}
