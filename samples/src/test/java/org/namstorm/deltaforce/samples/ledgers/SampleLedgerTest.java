package org.namstorm.deltaforce.samples.ledgers;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.StringWriter;

/**
 * Created by maxnamstorm on 9/8/2016.
 */

public class SampleLedgerTest extends TestCase {

    @Test
    public void testEditAndCommit() {
        SampleLedger ledger = new SampleLedger();

        ledger.setWriter(new StringWriter());
        ledger.open();

        ledger.editBuyer().setMetaValue("membership","123");

        ledger.editSalesPerson().setFirstName("Alejandro")
                .setLastName("Rodriguez")
                .setAge(32);


        ledger.commit();

        assertEquals("meta value check", "123", ledger.buyer().getMetaValue("membership"));
        assertEquals("salesPerson first alias check", "Alejandro", ledger.salesPerson().getFirstName());
        assertEquals("salesPerson lastname check", "Rodriguez", ledger.salesPerson().getLastName());
        assertEquals("salesPerson age check",  32, ledger.salesPerson().getAge());

    }

}