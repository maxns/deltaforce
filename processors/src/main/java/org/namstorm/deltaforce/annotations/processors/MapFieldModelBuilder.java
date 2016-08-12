package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import java.util.HashMap;
import java.util.Map;

import static javax.tools.Diagnostic.Kind;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public class MapFieldModelBuilder extends VariableFieldModelBuilder<MapFieldModel, Map> {

    public static final Class[] FIELD_BASE_CLASSES = {Map.class, HashMap.class};

    public MapFieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public MapFieldModel build() {

        MapFieldModel mapRes = new MapFieldModel();
        applyCommon(mapRes);

        DeclaredType ty = (DeclaredType) element.asType();


        mapRes.key = new VariableFieldModel();
        mapRes.key.type = "Object";
        mapRes.value = new VariableFieldModel();
        mapRes.value.type = "Object";
        mapRes.type = element.asType().toString();
        mapRes.boxedType = mapRes.type;

        // if we got T params
        if(onTypeArguments(
                ta -> mapRes.key = box(mapRes.key, ta),
                ta -> mapRes.value = box(mapRes.value, ta)
        )!=2) {
            printMessage(Kind.WARNING, "Got an irregular number of type args in Map, ignoring");
        }

        return mapRes;
    }


}
