package mock;

import org.namstorm.deltaforce.annotations.DeltaBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by maxnam-storm on 11/8/2016.
 */
@DeltaBuilder
public class AnnotatedPojo {
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

    public String getStringValue() {
        return stringValue;
    }

    String stringValue;
    int intValue;
    short shortValue;
    long longValue;
    byte byteValue;
    float floatValue;
    double doubleValue;

    @DeltaField(alias="-s;+Item")
    Collection<String> strings;

    @DeltaField(alias="-s")
    List<Integer> numbers = new java.util.ArrayList<>();

    @DeltaField(ignore = true)
    Map transientMap;

    @DeltaField(alias = "metaValue")
    HashMap<String,String> metaValues = new HashMap<>();


    private Collection<String> extraKeys;

    public Collection<String> getExtraKeys() { return extraKeys; }
    public void setExtraKeys(Collection<String> extraKeys) { this.extraKeys = extraKeys; }

    public String getMetaValue(String key) {
        return metaValues.get(key);
    }



}
