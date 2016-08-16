package org.namstorm.deltaforce.ledgers.buildable;

import org.namstorm.deltaforce.core.Buildable;
import org.namstorm.deltaforce.core.DeltaBuilder;
import org.namstorm.deltaforce.ledgers.AbstractDeltaLedger;
import org.namstorm.deltaforce.ledgers.AbstractDeltaLedgerBase;
import org.namstorm.deltaforce.ledgers.DeltaLedger;
import org.namstorm.deltaforce.ledgers.LedgerSchema;

/**
 * Created by maxnam-storm on 16/8/2016.
 *
 * This ledger works on the basis of having a delta buildable ledger entry
 *
 * The idea is to extend from this class
 *
 */
public class BuildableDeltaLedger<T extends BuildableLedgerEntry> implements DeltaLedger<T> {
    private LedgerController extends AbstractDeltaLedger<T> {

    }

    private DeltaBuilder<T> ledgerEntryBuilder;

    public void setLedgerEntryBuilder(DeltaBuilder<T> builder) {
        this.ledgerEntryBuilder = builder;
        initFields(builder);
    }

    /**
     * Initialises the fields
     * @param builder
     */
    protected void initFields(DeltaBuilder<T> builder) {
        builder.

    }

    @Override
    protected void startEdit() {

    }

    @Override
    protected void commitEdit() {

    }

    @Override
    protected void cancelEdit() {

    }
}
