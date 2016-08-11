package org.namstorm.deltaforce.annotations.processors;

import org.namstorm.deltaforce.annotations.DeltaField;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static javax.tools.Diagnostic.*;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public class MapFieldModelBuilder extends VariableModelBuilder<MapFieldModel, Map> {

    public static final Class[] FIELD_BASE_CLASSES = {Map.class, HashMap.class};

    public MapFieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public MapFieldModel build() {

        MapFieldModel mapRes = new MapFieldModel();
        applyCommon(mapRes);

        DeclaredType ty = (DeclaredType) element.asType();

        whenAnnotated(DeltaField.class, a ->
                mapRes.mapItem = a.mapItem()
        );

        mapRes.key = new FieldModelImpl();
        mapRes.key.type = "Object";
        mapRes.value = new FieldModelImpl();
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
