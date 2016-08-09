package org.namstorm.deltaforce.samples.ledgers;

import org.apache.commons.lang.SerializationUtils;
import org.namstorm.deltaforce.core.Delta;
import org.namstorm.deltaforce.ledgers.AbstractDeltaLedger;
import org.namstorm.deltaforce.ledgers.LedgerField;
import org.namstorm.deltaforce.ledgers.LedgerSchema;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by maxnamstorm on 9/8/2016.
 */
public abstract class SerializingDeltaLedger<T extends LedgerSchema> extends AbstractDeltaLedger<T> {

    private Writer writer;
    private StringBuffer buffer;
    public void setWriter(final Writer writer) {
        this.writer = writer;
    }
    @Override
    protected void startEdit() {
       buffer = new StringBuffer();

    }

    @Override
    protected void cancelEdit() {
        buffer = null;
    }

    @Override
    protected void commitDelta(LedgerField f, Delta delta) {
        super.commitDelta(f, delta);
        buffer.append(f.getFieldName())
                .append(delta);
    }

    @Override
    protected void commitEdit() {
        super.commitEdit();
        try {
            writer.write(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            cancelEdit();
        }
    }
}
