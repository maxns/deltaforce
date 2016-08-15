package org.namstorm.deltaforce.annotations.processors;

import org.namstorm.deltaforce.core.Buildable;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by maxnam-storm on 15/8/2016.
 */
public class BuildableFieldModelBuilder extends VariableFieldModelBuilder<BuildableFieldModel, Object> {
    public static final Class[] FIELD_BASE_CLASSES = {Buildable.class};

    public BuildableFieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public BuildableFieldModel build() {
        BuildableFieldModel res = new BuildableFieldModel();

        res = applyCommon(res);

        res.setBuilderClassName(
                element.asType().toString() + "Builder"
        );

        return res;
    }


}
