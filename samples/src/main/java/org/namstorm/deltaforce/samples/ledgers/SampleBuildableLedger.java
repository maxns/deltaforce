package org.namstorm.deltaforce.samples.ledgers;

import org.namstorm.deltaforce.core.DeltaBuilder;
import org.namstorm.deltaforce.ledgers.buildable.AbstractBuildableDeltaLedger;
import org.namstorm.deltaforce.samples.ledgers.model.SampleBuildableLedgerEntry;
import org.namstorm.deltaforce.samples.ledgers.model.SampleBuildableLedgerEntryBuilder;

/**
 * Created by maxnamstorm on 16/8/2016.
 */
public class SampleBuildableLedger extends AbstractBuildableDeltaLedger<SampleBuildableLedgerEntry> {
    private static Class BUILDER_CLASS;
    {
        try {
            BUILDER_CLASS = Class.forName("org.namstorm.deltaforce.samples.ledgers.model.SampleBuildableLedgerEntryBuilder");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public SampleBuildableLedger() {
        super();
        try {
            super.setLedgerEntryBuilder((DeltaBuilder<SampleBuildableLedgerEntry>) BUILDER_CLASS.newInstance());
        }catch (Exception e) {
            throw new IllegalStateException("Builder not found", e);
        }
    }

    public SampleBuildableLedgerEntryBuilder edit() {
        return (SampleBuildableLedgerEntryBuilder) super.edit();
    }
}
