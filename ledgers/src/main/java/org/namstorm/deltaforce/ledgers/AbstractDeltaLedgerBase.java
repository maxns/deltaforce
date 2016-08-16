package org.namstorm.deltaforce.ledgers;

/**
 * Created by maxnam-storm on 16/8/2016.
 */
public abstract class AbstractDeltaLedgerBase implements DeltaLedger {
    private Status status = Status.Closed;

    public AbstractDeltaLedgerBase() {
        super();
    }

    @Override
    public void open() {
        _assertClosed();

        setStatus(Status.Open);
        startEdit();
    }

    private void _assertClosed() {
        if (status != Status.Closed) {
            throw new IllegalStateException("Ledger is already open");
        }
    }

    @Override
    public void commit() {
        _assertOpen("Cannot commit if not open");

        commitEdit();
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

        cancelEdit();
        setStatus(Status.Closed);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Override to start editing
     */
    protected abstract void startEdit();

    protected abstract void commitEdit();

    /**
     * Override to provide edit cancle logic
     */
    protected abstract void cancelEdit();
}
