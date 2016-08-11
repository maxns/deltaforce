package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.lang.ObjectUtils;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by maxnam-storm on 11/8/2016.
 */
class MockTypeElement implements TypeElement {
    Class c;
    Annotation[] annotations;
    Annotation annotation;
    ElementKind elementKind;
    Set<Modifier> modifiers;
    Name simpleName;
    TypeMirror superClass;
    List<? extends TypeMirror> interfaces;
    List<? extends TypeParameterElement> typeParameters;
    Element enclosingElement;

    public MockTypeElement(Class c) {
        from(c);
    }

    public static MockTypeElement create(Class c) {

        MockTypeElement res = new MockTypeElement(c);
        
        return res;
    }

    private MockTypeElement from(Class c) {

        this.c = c;

        annotations = c.getAnnotations();


        return this;
    }


    @Override
    public List<? extends Element> getEnclosedElements() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        A[] res = getAnnotationsByType(annotationType);

        return res.length>0?res[0]:null;
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        List<A> res = new ArrayList<>();

        for(Annotation a:annotations) {
            if(a.getClass().equals(annotationType)) {
                res.add((A)a);
            }
        }


        return (A[]) res.toArray();
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NestingKind getNestingKind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name getQualifiedName() {
        throw new UnsupportedOperationException();

    }

    @Override
    public TypeMirror asType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ElementKind getKind() {
        return elementKind;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    @Override
    public Name getSimpleName() {
        return simpleName;
    }

    @Override
    public TypeMirror getSuperclass() {
        return superClass;
    }

    @Override
    public List<? extends TypeMirror> getInterfaces() {
        return interfaces;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return typeParameters;
    }

    @Override
    public Element getEnclosingElement() {
        return enclosingElement;
    }
}
