package org.namstorm.deltaforce.ledgers;

import org.namstorm.deltaforce.annotations.DeltaBuilder;
import org.namstorm.deltaforce.core.Delta;

import java.util.List;

/**
 * Created by maxnam-storm on 9/8/2016.
 *
 * Manages ledger state
 * Knows how to talk to builders and get their deltas
 *
 */
public abstract class AbstractDeltaLedger<T extends LedgerSchema> {

    enum Status {
        Open,
        Closed
    }

    private Status status = Status.Closed;
    protected T schema;

    protected T schema() { return schema; }

    public AbstractDeltaLedger() {

        super();
        schema = initSchema();
    }

    /**
     * override to initialise schema
     *
     * @return
     */
    protected abstract T initSchema();


    public void open() {
        _assertClosed();

        setStatus(Status.Open);
        startEdit();
    }

    private void _assertClosed() {
        if(status != Status.Closed) {
            throw new IllegalStateException("Ledger is already open");
        }
    }

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

    /**
     *
     */
    protected void commitEdit() {
        schema.fields().forEach(f -> {

            commitField((LedgerField<?>) f);
        });
    }

    /**
     * Commits individual field by collecting all deltas
     * @param f
     */
    private void commitField(LedgerField<?> f) {
        f.getBuilder().visitDeltas(delta -> {
            commitDelta(f, delta);
        });
    }

    /**
     * Override to provide handling of commit delta
     * @param f
     * @param delta
     */
    protected void commitDelta(LedgerField f, Delta delta) {
        f.getBuilder().apply(f.getFieldValue());
    }

    /**
     * Override to provide edit cancle logic
     */
    protected abstract void cancelEdit();


}