package org.namstorm.deltaforce.annotations.processors;

/**
 * Created by maxnam-storm on 15/8/2016.
 */
public class BuildableFieldModel extends VariableFieldModel {
    public static final String TYPE_FIELD = "buildable";

    public String getBuilderPackage() {
        return builderPackage;
    }

    private String builderPackage;

    @Override
    public String getAccessorType() {
        return TYPE_FIELD;
    }

    public String getBuilderClassName() {
        return builderClassName;
    }

    public void setBuilderClassName(String builderClassName) {
        this.builderClassName = builderClassName;
    }

    private String builderClassName;

    public void setBuilderPackage(String builderPackage) {
        this.builderPackage = builderPackage;
    }
    public String getBuilderFullClassName() {
        return getBuilderPackage()+"."+getBuilderClassName();
    }
}
