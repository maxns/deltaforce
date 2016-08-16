package org.namstorm.deltaforce.samples.ledgers;

import junit.framework.TestCase;
import org.junit.Test;
import org.namstorm.deltaforce.samples.ledgers.model.SampleBuildableLedgerEntry;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by maxnamstorm on 16/8/2016.
 */
public class SampleBuildableLedgerTest extends TestCase{

    @Test
    public void testBuildableCommit() {
        SampleBuildableLedger ledger = new SampleBuildableLedger();

        SampleBuildableLedgerEntry entry = ledger.open();

        ledger.edit().editAuthor().setFirstName("mocky").setLastName("mock");

        ledger.commit();

        assertEquals("author first name", "mocky", entry.getAuthor().getFirstName());
        assertEquals("author first name", "mock", entry.getAuthor().getLastName());

        SampleBuildableLedgerEntry newEntry = ledger.open();

        assertNotSame("new entry must be new", entry, newEntry);

        ledger.edit().addCredit(28.8).addDebit(18.8);

        ledger.commit();

        assertEquals("total must be 10", (double)10, newEntry.getTotal());



    }
}