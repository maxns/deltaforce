package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by maxnam-storm on 5/8/2016.
 * <p>
 * This one will
 */
public class MapFieldModel extends VariableFieldModel {
    public static final String TYPE_MAP = "map";
    @Override
    public String getAccessorType() {
        return TYPE_MAP;
    }


    public FieldModel getKey() {
        return key;
    }

    public FieldModel getValue() {
        return value;
    }

    VariableFieldModel key;
    VariableFieldModel value;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("key", key)
                .append("value", value)
                .toString();
    }
}
