/**
 * Copyright (c) 2018 CLSA Limited. All rights reserved.
 */
package org.namstorm.deltaforce.annotations.processors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Internal use element visitor
 */
class ElementVisitorAdapter implements ElementVisitor<TypeMirror, Void> {

    @Override
    public TypeMirror visit(Element e, Void p) {
        return null;
    }

    @Override
    public TypeMirror visit(Element e) {
        return null;
    }

    @Override
    public TypeMirror visitPackage(PackageElement e, Void p) {
        return null;
    }

    @Override
    public TypeMirror visitType(TypeElement e, Void p) {
        return null;
    }

    @Override
    public TypeMirror visitVariable(VariableElement e, Void p) {
        return null;
    }

    @Override
    public TypeMirror visitExecutable(ExecutableElement e, Void p) {
        return null;
    }

    @Override
    public TypeMirror visitTypeParameter(TypeParameterElement e, Void p) {
        return null;
    }

    @Override
    public TypeMirror visitUnknown(Element e, Void p) {
        return null;
    }


}
