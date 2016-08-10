package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.validator.Var;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
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
    private static Set[] BUILDER_BASECLASSES = {
            MapFieldModelBuilder.FIELD_BASE_CLASSES,
            FieldModelBuilder.FIELD_BASE_CLASSES
    };

    public FieldModelBuilderFactory() {
        super();
        initBuilderMap();
    }

    private List<Class> fieldBaseClasses;
    private Map<String, Class<? extends VariableModelBuilder>> builderMap;

    private void initBuilderMap() {
        fieldBaseClasses = new ArrayList<>();
        builderMap = new HashMap<>();

        for(Set<Class> cs:BUILDER_BASECLASSES) {
            cs.forEach(fieldBaseClass -> {
                builderMap.put(fieldBaseClass.getName(), fieldBaseClass.getDeclaringClass());
            });
        }
    }

    /**
     *
     * @param pe
     * @param e
     * @return
     */
    public VariableModelBuilder create(ProcessingEnvironment pe, VariableElement e) throws IllegalAccessException, InstantiationException {

        Class<? extends VariableModelBuilder> res = matchBuilder((TypeElement) e.getEnclosingElement());

        if(res==null) {
            throw new IllegalArgumentException("Could not match:" + e + " with any builders");
        }

        return res.newInstance();
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
     * @param typeElem
     * @return
     */
    public Class<? extends VariableModelBuilder> matchBuilder(TypeElement typeElem) {

        String fieldClass = typeElem.getClass().getName();

        Class<? extends VariableModelBuilder> res;

        if(builderMap.containsKey(fieldClass)) {
            res = builderMap.get(fieldClass);
        }else {
            DeclaredType parent = (DeclaredType) typeElem.getSuperclass();
            if(parent!=null) {
                res = matchBuilder((TypeElement)parent.asElement());
            }else {
                res = null;
            }

        }

        return res;
    }


}
