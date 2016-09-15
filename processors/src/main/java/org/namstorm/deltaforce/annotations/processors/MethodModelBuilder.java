package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.annotations.processors.util.DFUtil;

import com.sun.tools.javac.code.Symbol;

/**
 * MethodModelBuilder
 */
class MethodModelBuilder extends VariableFieldModelBuilder<FieldModel,Object> {

    private static final String IS = "is";

    private static final String GET = "get";

    /**
     *
     * @param processingEnvironment
     */
    MethodModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public FieldModel build() {
        VariableFieldModel res;

        res = new VariableFieldModel();

        res = applyCommon(res);

        res = box(res, ((Symbol.MethodSymbol) element).getReturnType());

        return res;
    }


    @Override
    public <A extends VariableFieldModel> A applyCommon(final A fieldModel) {
        fieldModel.accessible = false;

        String fieldName = element.getSimpleName().toString();
        if (fieldName.startsWith(GET))
            fieldName = fieldName.substring(3);
        else if (fieldName.startsWith(IS))
            fieldName = fieldName.substring(2);

        fieldModel.name = String.valueOf(fieldName.charAt(0)).toLowerCase() + fieldName.substring(1);
        fieldModel.accessorMethod = element.getSimpleName().toString();
        fieldModel.alias = DFUtil.compileAlias(fieldModel.name, "");
        fieldModel.type = ((Symbol.MethodSymbol) element).getReturnType().toString();

        box(fieldModel, ((Symbol.MethodSymbol) element).getReturnType());

        onAnnotation(DeltaField.class, a -> fieldModel.alias = DFUtil.compileAlias(fieldModel.name, a.alias()) );

        printMessage(Diagnostic.Kind.NOTE, "created field model:" + fieldModel.toString());

        return fieldModel;
    }

}
