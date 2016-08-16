package org.namstorm.deltaforce.samples.ledgers.model;

import org.namstorm.deltaforce.annotations.DeltaBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.ledgers.buildable.BuildableLedgerEntry;
import org.namstorm.deltaforce.samples.ComplexPerson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by maxnamstorm on 16/8/2016.
 */
@DeltaBuilder
public class SampleBuildableLedgerEntry implements BuildableLedgerEntry{

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
