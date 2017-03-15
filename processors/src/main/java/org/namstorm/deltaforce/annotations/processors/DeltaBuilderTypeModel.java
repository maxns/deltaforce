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

    /**
     * The actual class that implements the interface className. May have same value as className if
     * className is an actual class.
     */
    String classImplName;

    /** The class fully qualified alias. */
    String qualifiedName;

    String deltaBuilderClassName;

    String deltaBuilderQualifiedName;

    String extendClassName;

    String implementsInterfaces;

    boolean chainSetters = true;

    boolean addOverrideForAccessors = false;

    /**
     * whether we should chain the setters to return the builder instance
     *
     * @param chainSetters
     */
    public void setChainSetters(boolean chainSetters) {
        this.chainSetters = chainSetters;
    }

    /**
     * whether to use the Override annotation on the accessors
     *
     * @param addOverrideForAccessors
     */
    public void setAddOverrideForAccessors(boolean addOverrideForAccessors) {
        this.addOverrideForAccessors = addOverrideForAccessors;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     *
     * @param classImplName
     */
    public void setClassImplName(String classImplName) {
        this.classImplName = classImplName;
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
     * Getter for classImplName property.
     *
     * @return
     */
    public String getClassImplName() {
        return classImplName;
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

    /**
     * whether the generated class chains the Builder class on return type
     *
     * @return the property value
     */
    public boolean isChainSetters() {
        return chainSetters;
    }

    /**
     * Whether to add Override annotation for getters and setters
     *
     * @return the property value
     */
    public boolean isAddOverrideForAccessors() {
        return addOverrideForAccessors;
    }

}
