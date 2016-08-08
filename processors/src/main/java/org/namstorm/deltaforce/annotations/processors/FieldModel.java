package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by maxnamstorm on 4/8/2016.
 */
public class FieldModel {
    public static String TYPE_FIELD = "field";

    public String getAccessorType() { return TYPE_FIELD; }

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
