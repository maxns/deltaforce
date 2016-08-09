package org.namstorm.deltaforce.samples;

import junit.framework.TestCase;
import org.junit.Test;
import org.namstorm.deltaforce.core.Delta;

/**
 * Created by maxnam-storm on 5/8/2016.
 */
public class ComplexPersonTest extends TestCase {
    public static final byte VAL_BYTE = 'A';
    public static final double VAL_DOUBLE = 1.1f;
    public static final float VAL_FLOAT = 2.1f;
    public static final int VAL_INT = 2;
    public static final short VAL_SHORT = 3;
    public static final long VAL_LONG = 10_000;
    public static final int[] VAL_INT_ARR = new int[]{VAL_INT, VAL_INT + 1, VAL_INT + 2};
    public static final String VAL_META_NAME = "metaName";
    public static final String VAL_META_VALUE = "metaValue";
    public static final String VAL_META_NAME2 = "metaName2";
    public static final String VAL_META_VALUE2 = "metaValue2";


    @Test
    public void testSetters() throws Exception {
        ComplexPerson testPerson = new org.namstorm.deltaforce.samples.ComplexPersonBuilder(new ComplexPerson())
                .op(Delta.OP.ADD)
                .setByteValue(VAL_BYTE)
                .setDoubleValue(VAL_DOUBLE)
                .setFloatValue(VAL_FLOAT)
                .setIntValue(VAL_INT)
                .setIntValues(VAL_INT_ARR)
                .setShortValue(VAL_SHORT)
                .setLongValue(VAL_LONG)
                .setMetaValue(VAL_META_NAME,VAL_META_VALUE)
                .build();


        assertEquals(VAL_BYTE, testPerson.getByteValue());
        assertEquals(VAL_DOUBLE, testPerson.getDoubleValue());
        assertEquals(VAL_FLOAT, testPerson.getFloatValue());
        assertEquals(VAL_INT, testPerson.getIntValue());
        assertEquals(VAL_SHORT, testPerson.getShortValue());
        assertEquals(VAL_INT_ARR, testPerson.getIntValues());
        assertEquals(VAL_META_VALUE, testPerson.getMetaValue(VAL_META_NAME));

        //now lets see if meta values survive

        new org.namstorm.deltaforce.samples.ComplexPersonBuilder(testPerson)
                .setMetaValue(VAL_META_NAME2, VAL_META_VALUE2)
                .apply(testPerson);

        assertEquals("Remained untouched", VAL_META_VALUE, testPerson.getMetaValue(VAL_META_NAME));
        assertEquals("New meta value setup", VAL_META_VALUE2, testPerson.getMetaValue(VAL_META_NAME2));

        // now lets remove it

        new org.namstorm.deltaforce.samples.ComplexPersonBuilder(testPerson)
                .setMetaValue(VAL_META_NAME2, null)
                .apply(testPerson);

        assertEquals("Remained untouched", VAL_META_VALUE, testPerson.getMetaValue(VAL_META_NAME));
        assertNull("Nulling of " + VAL_META_NAME2, testPerson.getMetaValue(VAL_META_NAME2));


    }
}