package org.namstorm.deltaforce.annotations.processors;

import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.annotations.processors.util.DFUtil;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Consumer;

import static javax.tools.Diagnostic.Kind;

/**
 * Created by maxnam-storm on 10/8/2016.
 * <p>
 * Builds field models
 */
public abstract class VariableFieldModelBuilder<M extends FieldModel, BaseClass> extends ModelBuilder<VariableElement, M> {

    public VariableFieldModelBuilder() {
        super();
    }

    public VariableFieldModelBuilder(ProcessingEnvironment processingEnvironment) {
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

    protected <A extends VariableFieldModel> A box(A field, TypeMirror typeMirror) {

        field.type = typeMirror.toString();

        if (typeMirror.getKind().isPrimitive()) {
            field.primitive = true;

            printMessage(Kind.NOTE, "boxing:" + typeMirror.getKind().name());

            field.boxedType = autobox(typeMirror).getName();
            field.className = autobox(typeMirror).getSimpleName();

        } else {
            field.boxedType = field.type;
            field.className = (typeMirror instanceof DeclaredType)?((DeclaredType)typeMirror).asElement().getSimpleName().toString():typeMirror.toString();
            field.primitive = false;

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
    public <A extends VariableFieldModel> A applyCommon(final A fieldModel) {
        fieldModel.accessible = !element.getModifiers().contains(Modifier.PRIVATE);

        fieldModel.name = element.getSimpleName().toString();
        fieldModel.alias = DFUtil.compileAlias(fieldModel.name, "");
        fieldModel.type = element.asType().toString();

        box(fieldModel, element.asType());

        onAnnotation(DeltaField.class, a -> fieldModel.alias = DFUtil.compileAlias(fieldModel.name, a.alias()) );

        printMessage(Kind.NOTE, "created field model:" + fieldModel.toString());

        return fieldModel;
    }

    /**
     * Invokes taConsumer on each type argument, in sequence
     *
     * Useful to construct funky calls like this:
     *
     * onTypeArguments(
     *   ta -> mapRes.key = box(mapRes.key, ta),
     *   ta -> mapRes.value = box(mapRes.value, ta)
     *  );
     *
     * @param taConsumers
     * @return
     */
    protected int onTypeArguments(Consumer<TypeMirror>... taConsumers) {
        int res;

        List<? extends TypeMirror> Targs = ((DeclaredType)element.asType()).getTypeArguments();

        for(res=0; res<taConsumers.length && res<Targs.size(); res++) {
            taConsumers[res].accept( Targs.get(res) );
        }

        return res;
    }
}
