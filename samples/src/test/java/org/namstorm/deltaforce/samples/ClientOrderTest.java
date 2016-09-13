package org.namstorm.deltaforce.samples;

import org.junit.Test;
import org.namstorm.deltaforce.core.Delta;
import org.namstorm.deltaforce.core.DeltaMap;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * ClientOrderTest
 */
@SuppressWarnings("javadoc")
public class ClientOrderTest {

    @Test
    public void testSetAndGet() {
        ClientOrder order = new org.namstorm.deltaforce.samples.ClientOrderBuilder();
        order.setCurrencyCode("HKD");
        order.setQuantity(1000);
        order.setId(123456789);

        assertEquals("HKD", order.getCurrencyCode());
        assertEquals(1000, order.getQuantity(), 0.0);
        assertEquals(123456789, order.getId());
    }

    @Test
    public void testSetAndRollback() {
        org.namstorm.deltaforce.samples.ClientOrderBuilder builder = new org.namstorm.deltaforce.samples.ClientOrderBuilder();
        ClientOrder order = builder;
        order.setCurrencyCode("HKD");
        order.setQuantity(1000);
        order.setId(123456789);

        builder.rollback();

        assertNull(order.getCurrencyCode());
        assertEquals(0, order.getQuantity(), 0.0);
        assertEquals(0, order.getId());
    }

    @Test
    public void testApply() {
        org.namstorm.deltaforce.samples.ClientOrderBuilder builder = new org.namstorm.deltaforce.samples.ClientOrderBuilder();
        ClientOrder order = builder;
        order.setCurrencyCode("HKD");
        order.setQuantity(1000);
        order.setId(123456789);

        builder.applyAndClearDeltas();

        assertFalse(builder.containsDelta());

        assertEquals("HKD", order.getCurrencyCode());
        assertEquals(1000, order.getQuantity(), 0.0);
        assertEquals(123456789, order.getId());
    }

    @Test
    public void testApplyToAnotherObject() {
        org.namstorm.deltaforce.samples.ClientOrderBuilder builder = new org.namstorm.deltaforce.samples.ClientOrderBuilder();
        ClientOrder order = builder;
        order.setCurrencyCode("HKD");
        order.setQuantity(1000);
        order.setId(123456789);

        DeltaMap<String, Delta<?>> delta = builder.getDeltaMap();

        org.namstorm.deltaforce.samples.ClientOrderBuilder another = new org.namstorm.deltaforce.samples.ClientOrderBuilder();
        another.setDeltaMap(delta);
        another.applyAndClearDeltas();

        assertEquals("HKD", another.getCurrencyCode());
        assertEquals(1000, another.getQuantity(), 0.0);
        assertEquals(123456789, another.getId());
    }

}
