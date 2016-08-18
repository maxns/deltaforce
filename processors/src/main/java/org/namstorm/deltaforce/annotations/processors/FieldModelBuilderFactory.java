package org.namstorm.deltaforce.annotations.processors;

import org.namstorm.deltaforce.annotations.DeltaForceBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.annotations.processors.util.DFProcessorUtil;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxnamstorm on 10/8/2016.
 */
public class FieldModelBuilderFactory {
    public static final Class<BuildableFieldModelBuilder> BUILDABLE_FIELD_MODEL_BUILDER_CLASS = BuildableFieldModelBuilder.class;
    public static final Class<CollectionFieldModelBuilder> COLLECTION_FIELD_MODEL_BUILDER_CLASS = CollectionFieldModelBuilder.class;
    public static final Class<MapFieldModelBuilder> MAP_FIELD_MODEL_BUILDER_CLASS = MapFieldModelBuilder.class;
    public static final Class<FieldModelBuilder> FIELD_MODEL_BUILDER_CLASS = FieldModelBuilder.class;

    private static FieldModelBuilderFactory _instance;
    public static FieldModelBuilderFactory getInstance() {
        return _instance != null ? _instance : new FieldModelBuilderFactory();
    }

    /**
     * Temporary hack, until we make this configurable
     */
    private static Object[][] BUILDERS = {
            {MapFieldModelBuilder.FIELD_BASE_CLASSES, MAP_FIELD_MODEL_BUILDER_CLASS},
            {FieldModelBuilder.FIELD_BASE_CLASSES, FIELD_MODEL_BUILDER_CLASS},
            {CollectionFieldModelBuilder.FIELD_BASE_CLASSES, COLLECTION_FIELD_MODEL_BUILDER_CLASS},
            {BuildableFieldModelBuilder.FIELD_BASE_CLASSES, BUILDABLE_FIELD_MODEL_BUILDER_CLASS}

    };

    public FieldModelBuilderFactory() {
        super();
        initBuilderMap();
    }

    private Map<String, Class<? extends VariableFieldModelBuilder>> builderMap;

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
    public VariableFieldModelBuilder create(ProcessingEnvironment pe, VariableElement e)  {

        Class<? extends VariableFieldModelBuilder> res = matchBuilder(pe, e);

        if(res==null) {
            throw new IllegalArgumentException("Could not match:" + fmt(e) + " with any builders, check assignments");
        }



        try {
            return (VariableFieldModelBuilder) res.getConstructor(ProcessingEnvironment.class).newInstance(pe).with(e);

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

    private String fmt(VariableElement e) {
        return e.toString()+"["+ String.join(",", e.getKind().toString(), e.asType().toString(), e.getSimpleName());
    }


    /**
     * Attempts to match the right builder by:
     *  1. reading what the annotation say in its' type argument in its' @DeltaField field annotation
     *  2. if the class of the field itself is annotated with @DeltaForceBuilder
     *  3. finally match with something in the builder map
     *
     * @See autoMatchBuilder
     * @see org.namstorm.deltaforce.annotations.DeltaField.Type
     *
     * @param variableElement
     * @return
     */
    public Class<? extends VariableFieldModelBuilder> matchBuilder(ProcessingEnvironment pe, VariableElement variableElement) {
        Class<? extends VariableFieldModelBuilder> res = matchBuilderFomAnnotation(pe, variableElement);

        if(res!=null) {
            return res;
        }

        TypeMirror typeMirror = variableElement.asType();

        typeMirror = autobox(pe, typeMirror);

        if(!(typeMirror instanceof DeclaredType)) {
            return null;
        }

        return autoMatchBuilder(pe, (TypeElement) ((DeclaredType)typeMirror).asElement());
    }

    /**
     * Matches builder from annotation only
     * @param pe
     * @param variableElement
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    private Class<? extends VariableFieldModelBuilder> matchBuilderFomAnnotation(ProcessingEnvironment pe, VariableElement variableElement) {

        return (Class<? extends VariableFieldModelBuilder>) DFProcessorUtil.onAnnotations(variableElement, DeltaField.class, (Class) null)
                .select(ann -> {
                    switch(ann.type()) {
                        case FIELD: return FIELD_MODEL_BUILDER_CLASS;
                        case MAP: return MAP_FIELD_MODEL_BUILDER_CLASS;
                        case COLLECTION: return COLLECTION_FIELD_MODEL_BUILDER_CLASS;
                        case BUILDER: return BUILDABLE_FIELD_MODEL_BUILDER_CLASS;
                    }
                    return null;
                });
    }

    /**
     * Matches typeElem to the appropriate builder using "most closely matched" class / interface
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
     * @param pe
     * @param typeElem
     * @return
     */
    public Class<? extends VariableFieldModelBuilder> autoMatchBuilder(ProcessingEnvironment pe, TypeElement typeElem) {


        log(pe, Diagnostic.Kind.NOTE, "trying to match to (" + builderMap.toString() +")", typeElem);

        String fieldClass = typeElem.getQualifiedName().toString();

        Class<? extends VariableFieldModelBuilder> res = null;

        if(builderMap.containsKey(fieldClass)) {
            res = builderMap.get(fieldClass);
        }else {
            if(DFProcessorUtil.onAnnotations(typeElem, DeltaForceBuilder.class, Boolean.FALSE).select(a -> true)) {
                res = BUILDABLE_FIELD_MODEL_BUILDER_CLASS;
            }

        }


        if(res!=null) {
            log(pe, Diagnostic.Kind.NOTE, "match:" + res, typeElem);

        }else {
            // continue searching up the interface line
            if(res == null) {
                log(pe, Diagnostic.Kind.NOTE, "didn't match, will try interfaces:"+typeElem.getInterfaces(), typeElem);

                for(TypeMirror ti:typeElem.getInterfaces()) {
                    if(ti instanceof DeclaredType) {
                        res = autoMatchBuilder(pe, (TypeElement)((DeclaredType)ti).asElement() );
                        if(res!=null) break;
                    }

                }
            }
            if(res == null) {
                DeclaredType parent = typeElem.getSuperclass() instanceof DeclaredType ? (DeclaredType) typeElem.getSuperclass() : null;

                log(pe, Diagnostic.Kind.NOTE, "didn't match, will try parent:" + parent, typeElem);

                if (parent != null) {
                    res = autoMatchBuilder(pe, (TypeElement) parent.asElement());
                } else {
                    res = null;
                }
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
