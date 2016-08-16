package org.namstorm.deltaforce.ledgers;

import org.namstorm.deltaforce.annotations.DeltaBuilder;
import org.namstorm.deltaforce.core.AbstractDeltaBuilder;
import org.namstorm.deltaforce.core.Delta;
import org.namstorm.deltaforce.core.RecursiveDeltaMapVisitor;

import java.util.List;

/**
 * Created by maxnam-storm on 9/8/2016.
 * <p>
 * Manages ledger state
 * Knows how to talk to builders and get their deltas
 */
public abstract class AbstractDeltaLedger<T extends LedgerSchema> extends AbstractDeltaLedgerBase {

    protected T schema;

    protected T schema() {
        return schema;
    }

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


    /**
     * Cycles through the fields and calls commitField
     */
    @Override
    protected void commitEdit() {
        schema.fields().forEach(f -> {

            commitField((LedgerField) f);
        });

    }

    private abstract class CommitVisitor extends RecursiveDeltaMapVisitor {

        @Override
        public void visit(Delta<?> delta) {
            onDelta(delta);
            super.visit(delta);
        }
        abstract void onDelta(Delta<?> delta);

    }
    /**
     * Commits individual field by collecting all deltas within it using recursive delta visitor
     *
     * @param f
     */
    private void commitField(final LedgerField f) {
        f.getBuilder().apply(f.getFieldValue());

        f.getBuilder().visitDeltas(new CommitVisitor() {
            @Override
            void onDelta(Delta<?> delta) {
                AbstractDeltaLedger.this.onDelta(f, delta);
            }
        });
    }

    /**
     * Override to provide handling of commit delta
     *
     * @param f
     * @param delta
     */
    protected abstract void onDelta(LedgerField f, Delta delta);


}
