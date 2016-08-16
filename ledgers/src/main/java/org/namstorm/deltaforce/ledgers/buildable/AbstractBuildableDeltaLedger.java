package org.namstorm.deltaforce.ledgers.buildable;

import org.namstorm.deltaforce.core.Delta;
import org.namstorm.deltaforce.core.DeltaBuilder;
import org.namstorm.deltaforce.ledgers.AbstractDeltaLedgerBase;

/**
 * Created by maxnam-storm on 16/8/2016.
 *
 * This ledger works on the basis of having a delta buildable ledger entry
 *
 * The idea is to
 * - extend from this class to provide some specific ledger handling logic
 * - construct with a ledger entry that is delta buildable
 * - invoke "startEdit" that returns an EntryBuilder
 * - work with the entry builder to modify / create contents of the ledger entry
 * - when done, commit stuff which, in turn, collates all the deltas and invokes abstract methods to
 *      - validate
 *      - commit ledger entries
 *
 *
 */
public abstract class AbstractBuildableDeltaLedger<T extends BuildableLedgerEntry> extends AbstractDeltaLedgerBase<T>  {

    private DeltaBuilder<T> ledgerEntryBuilder;

    public void setLedgerEntryBuilder(DeltaBuilder<T> builder) {
        this.ledgerEntryBuilder = builder;
    }

    @Override
    protected T createEntry() {
        return ledgerEntryBuilder.create();
    }

    @Override
    public void commitDeltas() {
        ledgerEntryBuilder.apply(openEntry());
    }

    public DeltaBuilder<T> edit() {
        _assertOpen("Can't edit until open");

        return ledgerEntryBuilder.from(openEntry());
    }
}
