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
public abstract class AbstractDeltaLedger {

    enum Status {
        Open,
        Closed
    }

    private Status status = Status.Closed;
    private LedgerSchema<?> schema;

    public AbstractDeltaLedger(LedgerSchema schema) {

        super();
        setSchema(schema);

    }

    protected void setSchema(LedgerSchema<?> schema) {
        this.schema = initSchema(schema);
    }

    /**
     * override to initialise schema
     *
     * @param schema
     * @return
     */
    protected abstract LedgerSchema initSchema(LedgerSchema schema);


    public void open() {

        if(status == Status.Closed) {
            throw new IllegalStateException("Ledger is already open");
        }

        setStatus(Status.Open);
        startEdit();
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
     * Override to provide commit logic
     */
    protected void commitEdit() {
        for(LedgerField f:schema.fields()) {
            commitField(f);
        }
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

    protected abstract void commitDelta(LedgerField<?> f, Delta<?> delta);

    /**
     * Override to provide edit cancle logic
     */
    protected abstract void cancelEdit();


}
