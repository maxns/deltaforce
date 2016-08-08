package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by maxnam-storm on 5/8/2016.
 * <p>
 * This one will
 */
public class MapFieldModel extends FieldModel {
    public static final String TYPE_MAP = "map";
    @Override
    public String getAccessorType() {
        return TYPE_MAP;
    }

    public String getMapItem() {
        return mapItem;
    }

    public FieldModel getKey() {
        return key;
    }

    public FieldModel getValue() {
        return value;
    }

    String mapItem;

    FieldModel key;
    FieldModel value;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("key", key)
                .append("value", value)
                .toString();
    }
}
