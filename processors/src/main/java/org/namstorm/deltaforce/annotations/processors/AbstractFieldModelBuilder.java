package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.function.Consumer;

import static javax.tools.Diagnostic.*;

/**
 * Created by maxnam-storm on 10/8/2016.
 * <p>
 * Builds field models
 */
public abstract class AbstractFieldModelBuilder<T extends FieldModel> {

    protected VariableElement element;
    protected ProcessingEnvironment pe;

    public AbstractFieldModelBuilder() {
        super();
    }

    public AbstractFieldModelBuilder(ProcessingEnvironment processingEnvironment) {
        super();
        this.pe = processingEnvironment;
    }


    public AbstractFieldModelBuilder with(VariableElement ve) {
        element = ve;

        return this;
    }

    /**
     * Actually builds it
     * @return
     */
    public abstract T build();

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

    protected void printMessage(Kind kind, String msg) {
        pe.getMessager().printMessage(kind, msg, element);
    }


    protected <A extends Annotation> A whenAnnotated(Class<A> annotClass, Consumer<A> consumer) {

        A a = element.getAnnotation(annotClass);

        if(a!=null) {
            consumer.accept(a);
        }

        return a;
    }

    protected Class autobox(TypeMirror mirror) {

        switch (mirror.getKind()) {
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
                    return Class.forName(mirror.toString());
                } catch (ClassNotFoundException e) {
                    printMessage(Kind.ERROR, "Failed to autobox from " + mirror + ", e:" + e.toString());
                    return Object.class;
                }
        }
    }
}
