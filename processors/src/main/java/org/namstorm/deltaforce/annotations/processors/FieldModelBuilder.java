package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public class FieldModelBuilder extends VariableFieldModelBuilder<FieldModel,Object> {

    public static final Class[] FIELD_BASE_CLASSES = {Object.class};

    public FieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public FieldModel build() {
        VariableFieldModel res;

        res = new VariableFieldModel();

        res = applyCommon(res);

        res = box(res, element.asType());

        return res;
    }



}
