package org.namstorm.deltaforce.annotations.processors;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public interface FieldModel {
    String getAccessorType();

    String getBoxedType();

    boolean isAccessible();

    String getType();

    String getName();
}
