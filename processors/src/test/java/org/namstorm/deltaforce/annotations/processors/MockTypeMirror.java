package org.namstorm.deltaforce.annotations.processors;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by maxnam-storm on 11/8/2016.
 */
public class MockTypeMirror implements TypeMirror {
    private TypeKind kind;
    Annotation annotations[];





    @Override
    public TypeKind getKind() {
        return kind;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return null;
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return null;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return null;
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        return null;
    }
}
