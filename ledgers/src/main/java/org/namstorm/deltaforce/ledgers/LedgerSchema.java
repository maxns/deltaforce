package org.namstorm.deltaforce.ledgers;

import java.util.Set;

/**
 * Created by maxnam-storm on 9/8/2016.
 *
 */
public interface LedgerSchema<T extends LedgerField> {

    Set<T> fields();
}
