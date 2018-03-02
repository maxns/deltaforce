package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.annotations.processors.util.DFUtil;

/**
 * MethodModelBuilder
 */
class MethodModelBuilder extends VariableFieldModelBuilder<FieldModel, Object> {

    private static final String IS = "is";

    private static final String GET = "get";

    private boolean hasSetter = true;

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

        res = box(res, element.accept(returnTypeVisitor, null));

        return res;
    }

    MethodModelBuilder setHasSetter(boolean value) {
        hasSetter = value;
        return this;
    }

    @Override
    public <A extends VariableFieldModel> A applyCommon(final A fieldModel) {
        fieldModel.accessible = false;

        String fieldName = element.getSimpleName().toString();
        if (fieldName.startsWith(GET))
            fieldName = fieldName.substring(3);
        else if (fieldName.startsWith(IS))
            fieldName = fieldName.substring(2);

        fieldModel.name = String.valueOf(fieldName.charAt(0)).toLowerCase()
                + fieldName.substring(1);
        fieldModel.accessorMethod = element.getSimpleName().toString();
        fieldModel.hasSetter = hasSetter;
        fieldModel.alias = DFUtil.compileAlias(fieldModel.name, "");
        fieldModel.type = element.accept(returnTypeVisitor, null).toString();

        box(fieldModel, element.accept(returnTypeVisitor, null));

        onAnnotation(DeltaField.class,
                a -> fieldModel.alias = DFUtil.compileAlias(fieldModel.name, a.alias()));

        printMessage(Diagnostic.Kind.NOTE, "created field model:" + fieldModel.toString());

        return fieldModel;
    }

    private static final ReturnTypeVisitor returnTypeVisitor = new ReturnTypeVisitor();

    private static class ReturnTypeVisitor extends ElementVisitorAdapter {
        @Override
        public TypeMirror visitExecutable(ExecutableElement e, Void p) {
            return e.getReturnType();
        }
    }

}
