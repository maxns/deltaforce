package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public class FieldModelBuilder extends VariableModelBuilder<FieldModel,Object> {

    public static final Set<Class<Object>> FIELD_BASE_CLASSES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Object.class)));;

    public FieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public FieldModel build() {
        FieldModelImpl res;

        res = new FieldModelImpl();
        res = applyCommon(res);

        res = box(res, element.asType());

        return res;
    }



}
