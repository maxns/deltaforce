package org.namstorm.deltaforce.samples.ledgers;

import org.apache.commons.lang.SerializationUtils;
import org.namstorm.deltaforce.core.Delta;
import org.namstorm.deltaforce.core.DeltaMap;
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
    protected void onDelta(LedgerField f, Delta delta) {
        buffer.append(delta.getOp())
                .append(":").append(f.getFieldName()).append(":")
                .append(delta.getFieldName()).append(".").append(delta.getOp())
                .append("(").append(delta.getNewValue()).append(")");

    }

    @Override
    protected void commitEdit() {
        super.commitEdit();
        try {
            writer.write(buffer.toString());
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
            cancelEdit();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
