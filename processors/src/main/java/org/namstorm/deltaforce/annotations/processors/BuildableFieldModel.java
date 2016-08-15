package org.namstorm.deltaforce.annotations.processors;

/**
 * Created by maxnam-storm on 15/8/2016.
 */
public class BuildableFieldModel extends VariableFieldModel {
    public static String TYPE_FIELD = "buildable";

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

}
