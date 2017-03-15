package org.namstorm.deltaforce.samples.ledgers.model;

import org.namstorm.deltaforce.annotations.DeltaForceBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.core.DeltaBuilder;
import org.namstorm.deltaforce.ledgers.buildable.BuildableLedgerEntry;
import org.namstorm.deltaforce.samples.ComplexPerson;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by maxnamstorm on 16/8/2016.
 */
@DeltaForceBuilder(implement={"SampleBuildableLedgerEntry.Builder"})
public class SampleBuildableLedgerEntry implements BuildableLedgerEntry<SampleBuildableLedgerEntry,SampleBuildableLedgerEntry.Builder>{
    @DeltaField(ignore = true) private Builder builder;
    @Override
    public Builder getBuilder() {
        return builder;
    }

    @Override
    public void setBuilder(Builder builder) {
        this.builder = builder;
    }


    public interface Builder extends DeltaBuilder<SampleBuildableLedgerEntry> {
        Builder setAuthor(ComplexPerson author);

    }

    ComplexPerson author;
    Date timeStamp = new Date();
    @DeltaField(alias="-s")
    ArrayList<Double> debits = new ArrayList<>();

    @DeltaField(alias="-s")
    ArrayList<Double> credits = new ArrayList<>();

    public ComplexPerson getAuthor() {
        return author;
    }


    public Date getTimeStamp() {
        return timeStamp;
    }

    public ArrayList<Double> getDebits() {
        return debits;
    }

    public ArrayList<Double> getCredits() {
        return credits;
    }


    public Double getTotal() {
        final double[] res = {0};

        debits.forEach(d -> res[0] -=d );
        credits.forEach(c -> res[0] +=c );

        return res[0];
    }
}
