package org.namstorm.deltaforce.ledgers;

import org.namstorm.deltaforce.core.Delta;

/**
 * Created by maxnam-storm on 16/8/2016.
 */
public abstract class AbstractDeltaLedgerBase<T extends DeltaLedgerEntry> implements DeltaLedger<T> {
    private Status status = Status.Closed;

    public AbstractDeltaLedgerBase() {
        super();
    }

    @Override
    public T open() {
        _assertClosed();
        setStatus(Status.Open);
        return (this.openEntry = createEntry());
    }

    /**
     * Override to create an entry
     * @return
     */
    protected abstract T createEntry();

    private void _assertClosed() {
        if (status != Status.Closed) {
            throw new IllegalStateException("Ledger is already open");
        }
    }

    @Override
    public void commit() {
        _assertOpen("Cannot commit if not open");

        commitDeltas();
        setStatus(Status.Closed);
    }

    protected void _assertOpen(String s) {
        if (status != Status.Open) {
            throw new IllegalStateException(s);
        }
    }

    @Override
    public void cancel() {
        _assertOpen("Cannot cancel if not open");
        openEntry = null;
        setStatus(Status.Closed);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private T openEntry;

    protected T openEntry() {
        return openEntry;
    }


    /**
     * Cycles through the fields and calls commitField
     */
    protected abstract void commitDeltas();

}
