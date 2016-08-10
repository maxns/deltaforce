package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by maxnamstorm on 4/8/2016.
 */
public class FieldModelImpl implements FieldModel {
    public static String TYPE_FIELD = "field";

    @Override
    public String getAccessorType() { return TYPE_FIELD; }

    String name;
    String type;
    String boxedType;

    boolean accessible;
    boolean primitive;

    @Override
    public String getBoxedType() {
        return boxedType;
    }

    @Override
    public boolean isAccessible() {
        return accessible;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accessorType", getAccessorType())
                .append("name", name)
                .append("type", type)
                .append("primitive", primitive)
                .append("accessible", accessible)
                .toString();
    }
}
