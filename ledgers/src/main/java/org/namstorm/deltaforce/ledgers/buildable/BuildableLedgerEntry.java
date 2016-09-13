package org.namstorm.deltaforce.ledgers.buildable;

import org.namstorm.deltaforce.core.Buildable;
import org.namstorm.deltaforce.core.DeltaBuilder;
import org.namstorm.deltaforce.ledgers.DeltaLedgerEntry;

/**
 * Created by maxnam-storm on 16/8/2016.
 *
 * Derive from this interface to create a ledger entry
 *
 */
public interface BuildableLedgerEntry
        <T extends BuildableLedgerEntry, B extends DeltaBuilder<T>>
        extends DeltaLedgerEntry, Buildable<T, B> {


}
