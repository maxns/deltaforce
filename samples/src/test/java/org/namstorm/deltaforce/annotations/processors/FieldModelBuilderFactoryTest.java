package org.namstorm.deltaforce.annotations.processors;

import com.sun.tools.javac.code.Symbol;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by maxnamstorm on 10/8/2016.
 */
public class FieldModelBuilderFactoryTest {
    private class MockTypeElement implements TypeElement {
        Class c;
        Annotation[] annotations;
        Annotation annotation;
        ElementKind elementKind;
        Set<Modifier> modifiers;
        Name simpleName;
        TypeMirror superClass;
        List<? extends TypeMirror> interraces;
        List<? extends TypeParameterElement> typeParameters;
        Element enclosingElement;


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
            return (A) annotation;
        }

        @Override
        public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
            throw new UnsupportedOperationException();
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
            return interraces;
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

    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void matchBuilder() throws Exception {

        FieldModelBuilderFactory.getInstance().matchBuilder();
    }

}