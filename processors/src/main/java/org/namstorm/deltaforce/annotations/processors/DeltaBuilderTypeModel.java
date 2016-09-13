package org.namstorm.deltaforce.annotations.processors;

/**
 * Model for a type BeanInfo.
 *
 * @author deors
 * @version 1.0
 */
public class DeltaBuilderTypeModel {

    /** The package alias. */
    String packageName;

    /** The class simple alias. */
    String className;

    /** The class fully qualified alias. */
    String qualifiedName;

    String deltaBuilderClassName;

    String deltaBuilderQualifiedName;

    String extendClassName;

    String implementsInterfaces;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public void setDeltaBuilderClassName(String deltaBuilderClassName) {
        this.deltaBuilderClassName = deltaBuilderClassName;
    }

    public void setDeltaBuilderQualifiedName(String deltaBuilderQualifiedName) {
        this.deltaBuilderQualifiedName = deltaBuilderQualifiedName;
    }

    public String getExtendClassName() {
        return extendClassName;
    }

    public void setExtendClassName(String extendClassName) {
        this.extendClassName = extendClassName;
    }

    public String getImplementsInterfaces() {
        return implementsInterfaces;
    }

    public void setImplementsInterfaces(String implementsInterfaces) {
        this.implementsInterfaces = implementsInterfaces;
    }

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
