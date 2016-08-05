package org.namstorm.deltaforce.annotations.processors;

/**
 * Model for a type BeanInfo.
 *
 * @author deors
 * @version 1.0
 */
public class DeltaBuilderTypeModel {

    /** The package name. */
    String packageName;

    /** The class simple name. */
    String className;

    /** The class fully qualified name. */
    String qualifiedName;

    String deltaBuilderClassName;

    String deltaBuilderQualifiedName;

    /**
     * Default constructor.
     */
    public DeltaBuilderTypeModel() {
        super();
    }

    /**
     * Getter for packageName property.
     *
     * @return the property value.
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Getter for className property.
     *
     * @return the property value.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Getter for qualifiedName property.
     *
     * @return the property value.
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Getter for deltaBuilderClassName property.
     *
     * @return the property value.
     */
    public String getDeltaBuilderClassName() {
        return deltaBuilderClassName;
    }

    /**
     * Getter for deltaBuilderQualifiedName property.
     *
     * @return the property value.
     */
    public String getDeltaBuilderQualifiedName() {
        return deltaBuilderQualifiedName;
    }

}
