package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.validator.Var;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by maxnamstorm on 10/8/2016.
 */
public class FieldModelBuilderFactory {
    private static FieldModelBuilderFactory _instance;
    public static FieldModelBuilderFactory getInstance() {
        return _instance != null ? _instance : new FieldModelBuilderFactory();
    }

    /**
     * Temporary hack, until we make this configurable
     */
    private static Object[][] BUILDERS = {
            {MapFieldModelBuilder.FIELD_BASE_CLASSES, MapFieldModelBuilder.class},
            {FieldModelBuilder.FIELD_BASE_CLASSES, FieldModelBuilder.class}
    };

    public FieldModelBuilderFactory() {
        super();
        initBuilderMap();
    }

    private Map<String, Class<? extends VariableModelBuilder>> builderMap;

    private void initBuilderMap() {
        builderMap = new HashMap<>();

        for(int i=0; i<BUILDERS.length; i++) {
            Class<FieldModelBuilder> builderClass = (Class<FieldModelBuilder>) BUILDERS[i][1];

            Class[] builderFieldClasses = (Class[]) BUILDERS[i][0];

            for(Class cs:builderFieldClasses) {
                builderMap.put(cs.getName(), builderClass);
            }
        }

    }

    /**
     *
     * @param pe
     * @param e
     * @return
     */
    public VariableModelBuilder create(ProcessingEnvironment pe, VariableElement e)  {

        Class<? extends VariableModelBuilder> res = matchBuilder(pe, e);

        if(res==null) {
            throw new IllegalArgumentException("Could not match:" + e + " with any builders");
        }



        try {
            return (VariableModelBuilder) res.getConstructor(ProcessingEnvironment.class).newInstance(pe).with(e);

        } catch (InstantiationException e1) {
            log(pe, Diagnostic.Kind.ERROR, "Failed to instantiate:" + res + ", due to:" + e1, e);
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            log(pe, Diagnostic.Kind.ERROR, "Failed to instantiate:" + res + ", due to:" + e1, e);
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            log(pe, Diagnostic.Kind.ERROR, "Failed to instantiate:" + res + ", due to:" + e1, e);
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            log(pe, Diagnostic.Kind.ERROR, "Failed to find constructor with (ProcessingEnvironment as arg):" + res + ", due to:" + e1, e);
            e1.printStackTrace();
        }
        return null;
    }


    /**
     * Matches typeMirror to the builder using "most closely matched" class
     * e.g.
     *  let's say we have these builders with these field base classes
     *
     *  FieldModelBuilder:java.lang.Object
     *  CollectionModelBuilder:java.util.Collection
     *  SetModelBuilder:java.util.Set
     *  Map:java.util.Map
     *
     *  BuilderFactory would have built a tree on startup, like so
     *  Object
     *      Collection
     *          Set
     *          Map
     *
     *  TypeMirror is of type SuperDuperMap, it's hierarchy is
     *  Object
     *      Collection
     *          Set
     *          Map
     *              HashMap
     *                      SuperMaop
     *                              SuperDuperMap
     *
     *
     *  so we'll start at the bottom of the TypeMirror tree and traverse until we find one that we can find
     *  it in the
     *
     *
     * @param variableElement
     * @return
     */
    public Class<? extends VariableModelBuilder> matchBuilder(ProcessingEnvironment pe, VariableElement variableElement) {
        TypeMirror typeMirror = variableElement.asType();


        typeMirror = autobox(pe, typeMirror);


        if(!(typeMirror instanceof DeclaredType)) {
            return null;
        }

        return matchBuilder(pe, (TypeElement) ((DeclaredType)typeMirror).asElement());
    }

    public Class<? extends VariableModelBuilder> matchBuilder(ProcessingEnvironment pe, TypeElement typeElem) {

        log(pe, Diagnostic.Kind.NOTE, "trying to match to (" + builderMap.toString() +")", typeElem);

        String fieldClass = typeElem.getQualifiedName().toString();

        Class<? extends VariableModelBuilder> res = null;

        if(builderMap.containsKey(fieldClass)) {
            res = builderMap.get(fieldClass);

        }

        if(res!=null) {
            log(pe, Diagnostic.Kind.NOTE, "match:" + res, typeElem);

        }else {

            DeclaredType parent = typeElem.getSuperclass() instanceof DeclaredType ? (DeclaredType) typeElem.getSuperclass() : null;

            log(pe, Diagnostic.Kind.NOTE, "didn't match, will try parent:" + parent, typeElem);

            if(parent!=null) {
                res = matchBuilder(pe, (TypeElement)parent.asElement());
                // continue searching up the interface line
                if(res == null) {
                    log(pe, Diagnostic.Kind.NOTE, "didn't match, will try interfaces", typeElem);

                    for(TypeMirror ti:typeElem.getInterfaces()) {
                        if(ti instanceof DeclaredType) {
                            res = matchBuilder(pe, (TypeElement)((DeclaredType)ti).asElement() );
                            if(res!=null) break;
                        }

                    }
                }
            }else {
                res = null;
            }

        }

        return res;
    }
    private void log(ProcessingEnvironment pe, Diagnostic.Kind kind, String msg, Element elem) {
        pe.getMessager()
                .printMessage(kind, msg, elem);
    }

    public static TypeMirror autobox(ProcessingEnvironment pe, TypeMirror typeMirror) {

        TypeMirror res;

        if (typeMirror.getKind().isPrimitive()) {
            res = pe.getTypeUtils().boxedClass((PrimitiveType) typeMirror).asType();

        } else if(typeMirror.getKind() == TypeKind.ARRAY){
            res = pe.getTypeUtils().capture(typeMirror);
        }else {
            res = typeMirror;
        }

        return res;
    }


}
