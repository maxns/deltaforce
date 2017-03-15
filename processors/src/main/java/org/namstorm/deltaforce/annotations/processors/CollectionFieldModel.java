package org.namstorm.deltaforce.annotations.processors;


/**
 * Created by maxnam-storm on 10/8/2016.
 *
 * Describes a collection type field, to support operations:
 *
 * addItem
 * removeItem
 *
 *
 *
 */
public class CollectionFieldModel extends VariableFieldModel {
    public static final String TYPE_COLLECTION = "collection";

    public VariableFieldModel getValue() {
        return value;
    }

    /**
     * This an archetype of the values inside collection
     */
    VariableFieldModel value;


    @Override
    public String getAccessorType() {
        return TYPE_COLLECTION;
    }



}
