package org.namstorm.deltaforce.annotations.processors;

/**
 * Created by maxnamstorm on 4/8/2016.
 */
public class FieldModel {
    String name;
    String type;
    String boxedType;
    boolean accessible;
    boolean primitive;

    public String getBoxedType() {
        return boxedType;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }


}
