package org.namstorm.deltaforce.annotations.processors;

import java.util.List;

/**
 * Model for a method BeanInfo.
 *
 * @author deors
 * @version 1.0
 */
public class DeltaBuilderSetterMethodModel {

    /** Method name. */
    String name;

    /** Method qualified arg type */
    String qualifiedType;

    /**
     * Default constructor.
     */
    public DeltaBuilderSetterMethodModel() {
        super();
    }

    /**
     * Getter for name property.
     *
     * @return the property value.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for qualifiedType property.
     *
     * @return the property value.
     */
    public String getQualifiedType() {
        return qualifiedType;
    }



}
