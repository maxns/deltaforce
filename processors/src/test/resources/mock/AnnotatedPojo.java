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

    public AnnotatedPojo() {super();}

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

    public Collection<String> getExtraKeys() { return extraKeys; }
    public void setExtraKeys(Collection<String> extraKeys) { this.extraKeys = extraKeys; }

    public String getMetaValue(String key) {
        return metaValues.get(key);
    }

    public AnnotatedPojo getParent() {
        return parent;
    }

    public void setParent(AnnotatedPojo parent) {
        this.parent = parent;
    }

    public AnnotatedPojo getChild() {
        return child;
    }

    public void setChild(AnnotatedPojo child) {
        this.child = child;
    }

    /**
     * Simple values
     */
    String stringValue;
    int intValue;
    short shortValue;
    long longValue;
    byte byteValue;
    float floatValue;
    double doubleValue;

    /**
     * override default behaviour and don't map it to a builder
     */
    @DeltaField(type = DeltaField.Type.FIELD, alias = "")
    private AnnotatedPojo parent;


    /**
     * THis will be mapped to a builder
     */
    private AnnotatedPojo child;

    /**
     * Collection types
     */
    @DeltaField(alias="-s;+Item")
    Collection<String> strings;

    @DeltaField(alias="-s")
    List<Integer> numbers = new java.util.ArrayList<>();

    private Collection<String> extraKeys;

    /**
     * Maps
     */
    @DeltaField(ignore = true)
    Map transientMap;

    @DeltaField(alias = "metaValue")
    HashMap<String,String> metaValues = new HashMap<>();





}
