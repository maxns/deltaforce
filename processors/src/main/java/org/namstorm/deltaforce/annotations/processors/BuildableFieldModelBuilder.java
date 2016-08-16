package org.namstorm.deltaforce.annotations.processors;

import org.namstorm.deltaforce.core.Buildable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;

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
                ((DeclaredType)element.asType()).asElement().getSimpleName().toString() + "Builder"
        );

        return res;
    }


}
