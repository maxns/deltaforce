package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Array;
import java.util.Set;

import static javax.tools.Diagnostic.Kind;

/**
 * Created by maxnam-storm on 10/8/2016.
 * <p>
 * Builds field models
 */
public abstract class VariableModelBuilder<M extends FieldModel, BaseClass> extends ModelBuilder<VariableElement, M> {

    public VariableModelBuilder() {
        super();
    }

    public VariableModelBuilder(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
        }

    /**
     * Actually builds it
     * @return
     */
    public abstract M build();


    /**
     * boex it up
     * @param field
     * @param typeMirror
     * @param <A>
     * @return
     */

    protected <A extends FieldModelImpl> A box(A field, TypeMirror typeMirror) {

        field.type = typeMirror.toString();

        if (typeMirror.getKind().isPrimitive()) {
            field.primitive = true;

            printMessage(Kind.NOTE, "boxing:" + typeMirror.getKind().name());

            field.boxedType = autobox(typeMirror).getName();

        } else {
            field.boxedType = field.type;

            field.primitive = true;

        }
        return field;

    }

    /**
     * uses processEnvironment to print a {msg} of {kind}
     *
     * @param kind
     * @param msg
     */
    protected void printMessage(Kind kind, String msg) {
        pe.getMessager().printMessage(kind, msg, element);
    }


    /**
     * Autoboxes from the typemirror
     *
     * @param type
     * @return
     */
    public static Class autobox(TypeMirror type) {

        switch (type.getKind()) {
            case INT:
                return Integer.class;
            case ARRAY:
                return Array.class;
            case BOOLEAN:
                return Boolean.class;
            case DOUBLE:
                return Double.class;
            case BYTE:
                return Byte.class;
            case CHAR:
                return Character.class;
            case FLOAT:
                return Float.class;
            case LONG:
                return Long.class;
            case SHORT:
                return Short.class;
            case ERROR:
                return Error.class;
            default:
                try {
                    return Class.forName(type.toString());
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Failed to autobox:" + type, e);

                }
        }
    }

    /**
     * Applies common fields
     *
     * @param fieldModel mutable FieldModel
     * @return fieldModel
     */
    public <A extends FieldModelImpl> A applyCommon(final A fieldModel) {
        fieldModel.accessible = !element.getModifiers().contains(Modifier.PRIVATE);

        fieldModel.name = element.getSimpleName().toString();

        printMessage(Kind.NOTE, "created field model:" + fieldModel.toString());

        return fieldModel;
    }
}
