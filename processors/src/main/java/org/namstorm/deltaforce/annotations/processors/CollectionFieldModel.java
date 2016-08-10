package org.namstorm.deltaforce.annotations.processors;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public class CollectionFieldModel extends FieldModelImpl {
    public static final String TYPE_COLLECTION = "collection";


    @Override
    public String getAccessorType() {
        return TYPE_COLLECTION;
    }
}
