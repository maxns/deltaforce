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
    public static final String VAL_META_NAME = "metaName";
    public static final String VAL_META_VALUE = "metaValue";
    public static final String VAL_META_NAME2 = "metaName2";
    public static final String VAL_META_VALUE2 = "metaValue2";
    public static final String VAL_NICKNAME1 = "nickName1";
    public static final String VAL_NICKNAME2 = "nickName2";


    @Test
    public void testSetters() throws Exception {
        ComplexPerson testPerson = new org.namstorm.deltaforce.samples.ComplexPersonBuilder(new ComplexPerson())
                .op(Delta.OP.ADD)
                .setByteValue(VAL_BYTE)
                .setDoubleValue(VAL_DOUBLE)
                .setFloatValue(VAL_FLOAT)
                .setIntValue(VAL_INT)
                .addNickName(VAL_NICKNAME1)
                .addNickName(VAL_NICKNAME2)
                .setShortValue(VAL_SHORT)
                .setLongValue(VAL_LONG)
                .setMetaValue(VAL_META_NAME,VAL_META_VALUE)
                .build();


        assertEquals(VAL_BYTE, testPerson.getByteValue());
        assertEquals(VAL_DOUBLE, testPerson.getDoubleValue());
        assertEquals(VAL_FLOAT, testPerson.getFloatValue());
        assertEquals(VAL_INT, testPerson.getIntValue());
        assertEquals(VAL_SHORT, testPerson.getShortValue());

        assertTrue(VAL_NICKNAME1, testPerson.getNickNames().contains(VAL_NICKNAME1));
        assertTrue(VAL_NICKNAME2, testPerson.getNickNames().contains(VAL_NICKNAME2));

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
    public void testCollections() throws Exception {
        ComplexPerson testPerson = new org.namstorm.deltaforce.samples.ComplexPersonBuilder(new ComplexPerson())
                .addNickName(VAL_NICKNAME1)
                .addNickName(VAL_NICKNAME2)
                .addRating(123)
                .build();

        assertTrue(VAL_NICKNAME1, testPerson.getNickNames().contains(VAL_NICKNAME1));
        assertTrue(VAL_NICKNAME2, testPerson.getNickNames().contains(VAL_NICKNAME2));
        assertTrue(VAL_NICKNAME2, testPerson.getRatingNumbers().contains(123));

        //now lets see if removal works

        new org.namstorm.deltaforce.samples.ComplexPersonBuilder(testPerson)
                .removeNickName(VAL_NICKNAME1)
                .apply(testPerson);

        // removed?
        assertFalse(VAL_NICKNAME1 + " should not survive", testPerson.getNickNames().contains(VAL_NICKNAME1));

        // survived?
        assertTrue(VAL_NICKNAME2 + " should survive", testPerson.getNickNames().contains(VAL_NICKNAME2));




    }
}