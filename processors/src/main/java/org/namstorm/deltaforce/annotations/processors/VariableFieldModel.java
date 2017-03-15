package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by maxnamstorm on 4/8/2016.
 */
public class VariableFieldModel implements FieldModel {
    public static final String TYPE_FIELD = "field";

    @Override
    public String getAccessorType() { return TYPE_FIELD; }

    String name;

    String alias;
    String type;
    String boxedType;

    String className;

    boolean accessible;
    boolean primitive;

    String accessorMethod;
    boolean hasSetter = true;

    @Override
    public String getBoxedType() {
        return boxedType;
    }

    @Override
    public boolean getAccessible() {
        return accessible;
    }

    @Override
    public String getAccessor() {
        return accessorMethod;
    }

    @Override
    public boolean getHasSetter() {
        return hasSetter;
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
    public String getAlias() { return alias; }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accessorType", getAccessorType())
                .append("name", name)
                .append("alias", alias)
                .append("type", type)
                .append("primitive", primitive)
                .append("accessible", accessible)
                .toString();
    }
}
