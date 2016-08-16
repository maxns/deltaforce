package org.namstorm.deltaforce.ledgers;

/**
 * Created by maxnam-storm on 16/8/2016.
 *
 * DeltaLedger - defines contract of a delta ledger
 *
 *
 *
 *
 */
public interface DeltaLedger<T extends DeltaLedgerEntry> {
    enum Status {
        Open,
        Closed
    }
    /**
     * Opens the ledger for editing and returns first delta ledger entry
     *
     */
    T open();

    /**
     * commits open ledger
     */
    void commit();

    /**
     * cancels
     */
    void cancel();

}
