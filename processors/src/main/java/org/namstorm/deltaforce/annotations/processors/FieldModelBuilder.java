package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import static javax.tools.Diagnostic.*;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public class FieldModelBuilder extends AbstractFieldModelBuilder<FieldModel> {

    public FieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public FieldModel build() {
        FieldModel res;

        res = new FieldModelImpl();
        res = applyCommon(res);

        res = box(res, element.asType());

        return res;
    }

    public <F extends FieldModelImpl> F applyCommon(final F res) {
        res.accessible = !element.getModifiers().contains(Modifier.PRIVATE);

        res.name = element.getSimpleName().toString();

        printMessage(Kind.NOTE, "created field model:" + res.toString());

        return res;
    }
}
