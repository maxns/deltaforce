package org.namstorm.deltaforce.samples.ledgers;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.PrintWriter;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by maxnamstorm on 9/8/2016.
 */

public class SampleLedgerTest extends TestCase {

    @Test
    public void testEditAndCommit() {
        SampleLedger ledger = new SampleLedger();

        ledger.setWriter(new PrintWriter(System.out));
        ledger.open();

        ledger.editBuyer().setMetaValue("membership","123");

        ledger.commit();

        assertEquals("meta value check", "123", ledger.buyer().getMetaValue("membership"));
    }

}