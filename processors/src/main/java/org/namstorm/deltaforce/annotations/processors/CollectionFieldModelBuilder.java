package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.Collection;

/**
 * Created by maxnamstorm on 11/8/2016.
 */
public class CollectionFieldModelBuilder extends VariableModelBuilder<CollectionFieldModel, Collection> {

    public CollectionFieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public CollectionFieldModel build() {
        CollectionFieldModel res = new CollectionFieldModel();
        applyCommon(res);

        res.value = new FieldModelImpl();
        res.value.type = "Object";
        

        // if we got T params
        if(onTypeArguments(
                ta -> res.value = box(res.value, ta)
        )!=1) {
            printMessage(Diagnostic.Kind.WARNING, "Got an irregular number of type args in Collection, ignoring");
        }


        return res;
    }
}
