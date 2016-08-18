package org.namstorm.deltaforce.samples;

import org.namstorm.deltaforce.annotations.DeltaForceBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.core.Buildable;

import java.util.*;

/**
 * Created by maxnam-storm on 5/8/2016.
 */
@DeltaForceBuilder(implement = {"ComplexPerson.Builder"})
public class ComplexPerson extends Person implements Buildable<ComplexPerson, ComplexPerson.Builder>{
    interface Builder extends org.namstorm.deltaforce.core.DeltaBuilder<ComplexPerson>{

    }

    @DeltaField(ignore = true)
    private transient Builder builder;

    @Override
    public Builder getBuilder() {
        return builder;
    }

    @Override
    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

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

    public String getMetaValue(String key) {
        return metaValues.get(key);
    }


    public List<String> getNickNames() {
        return nickNames;
    }

    public Set<Number> getRatingNumbers() {
        return ratingNumbers;
    }



    int intValue;
    short shortValue;
    long longValue;
    byte byteValue;
    float floatValue;
    double doubleValue;

    @DeltaField(alias = "-s")
    List<String> nickNames;

    @DeltaField(alias = "rating")
    Set<Number> ratingNumbers = new HashSet<>();


    @DeltaField(ignore = true)
    Map transientMap;

    @DeltaField(alias = "metaValue")
    HashMap<String,String> metaValues = new HashMap<>();

    Person publicPerson;

    public Person getPrivatePerson() {
        return privatePerson;
    }

    public void setPrivatePerson(Person privatePerson) {
        this.privatePerson = privatePerson;
    }

    private Person privatePerson;


    ComplexPerson brother;

    public ComplexPerson getNonBuildingBrother() {
        return nonBuildingBrother;
    }

    public void setNonBuildingBrother(ComplexPerson nonBuildingBrother) {
        this.nonBuildingBrother = nonBuildingBrother;
    }

    @DeltaField(type=DeltaField.Type.FIELD)
    private ComplexPerson nonBuildingBrother;

}
