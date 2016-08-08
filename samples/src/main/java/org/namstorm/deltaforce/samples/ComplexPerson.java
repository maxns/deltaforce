package org.namstorm.deltaforce.samples;

import org.namstorm.deltaforce.annotations.DeltaBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxnam-storm on 5/8/2016.
 */
@DeltaBuilder
public class ComplexPerson {
    public int getIntValue() {
        return intValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public int[] getValues() {
        return intValues;
    }


    int intValue;
    short shortValue;
    long longValue;
    byte byteValue;
    float floatValue;
    double doubleValue;
    int[] intValues;
    private int[] privateIntValues;

    public void setPrivateIntValues(int[] privateIntValues) {
        this.privateIntValues = privateIntValues;
    }

    public int[] getIntValues() {

        return intValues;
    }

    public int[] getPrivateIntValues() {
        return privateIntValues;
    }

    @DeltaField(ignore = true)
    Map transientMap;

    @DeltaField(mapItem = "metaValue")
    HashMap<String,String> metaValues = new HashMap<>();

    public String getMetaValue(String key) {
        return metaValues.get(key);
    }


}
