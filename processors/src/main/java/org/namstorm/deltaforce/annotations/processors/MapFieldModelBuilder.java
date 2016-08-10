package org.namstorm.deltaforce.annotations.processors;

import org.namstorm.deltaforce.annotations.DeltaField;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static javax.tools.Diagnostic.*;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public class MapFieldModelBuilder extends FieldModelBuilder {
    public MapFieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public FieldModel build() {

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
        List<? extends TypeMirror> Targs = ty.getTypeArguments();

        if (Targs == null || Targs.size() == 0) {
            printMessage(Kind.WARNING, "No Type args specified for map");

        }else if(Targs.size() == 2) {
            mapRes.key = box(mapRes.key, Targs.get(0));
            mapRes.value = box(mapRes.value, Targs.get(1));

        } else {
            printMessage(Kind.WARNING, "Got an irregular number of type args in Map, ignoring");
        }

        return mapRes;
    }

}
