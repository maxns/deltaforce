package org.namstorm.deltaforce.annotations.processors;

import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.DisplayTool;
import org.namstorm.deltaforce.annotations.DeltaBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

/**
 * Annotation processors for DeltaBuilder annotation type. It generates a full featured
 * DeltaBuilder type with the help of an Apache Velocity template.
 *
 * @version 1.0
 */
public class DeltaBuilderProcessor
        extends AbstractProcessor {

    /**
     * keeping it plain, avoiding Android issues
     */
    static HashSet<String> SUPPORTED_TYPES = new HashSet<>(Arrays.asList("org.namstorm.deltaforce.annotations.DeltaBuilder"));
    private Properties properties;
    private VelocityEngine velocityEngine;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * Default constructor.
     */
    public DeltaBuilderProcessor() {
        super();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Initialising " + this.toString());

        try {
            properties = new Properties();
            URL url = this.getClass().getClassLoader().getResource("velocity.properties");
            properties.load(url.openStream());

            velocityEngine = new VelocityEngine(properties);
            velocityEngine.init();

        } catch (Exception e) {
            printError("Failed to initialise velocity engine:" + e.toString(), null);
        }
    }


    /**
     * Reads the DeltaBuilder information and writes a full featured
     * DeltaBuilder type with the help of an Apache Velocity template.
     *
     * @param annotations set of annotations found
     * @param roundEnv    the environment for this processors round
     * @return whether a new processors round would be needed
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (annotations.isEmpty()) {
            return true;
        }
        Element ce = null;

        HashMap<DeltaBuilderTypeModel, Map<String, FieldModel>> modelMap = new HashMap<>();

        try {
            DeltaBuilderTypeModel model = null;
            for (Element elem : roundEnv.getElementsAnnotatedWith(DeltaBuilder.class)) {
                ce = elem;

                if (elem.getKind() == ElementKind.CLASS) {

                    model = new DeltaBuilderTypeModel();
                    Map<String, FieldModel> fields = new HashMap<>();
                    modelMap.put(model, fields);

                    TypeElement classElement = (TypeElement) elem;
                    PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
                    DeltaBuilder annotation = classElement.getAnnotation(DeltaBuilder.class);

                    model.packageName = packageElement.getQualifiedName().toString();
                    model.className = classElement.getSimpleName().toString();
                    model.qualifiedName = classElement.getQualifiedName().toString();
                    model.deltaBuilderClassName = model.className + annotation.builderNameSuffix();

                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.NOTE,
                            "annotating class: " + model.qualifiedName, elem);

                    collectFields(classElement, fields, !annotation.ignoreInherited());


                }
            }

            if (modelMap.size() > 0) {

                for (DeltaBuilderTypeModel tm : modelMap.keySet()) {
                    writeBuilder(tm, modelMap.get(tm));
                }

            }
        } catch (ResourceNotFoundException rnfe) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    rnfe.getLocalizedMessage());
        } catch (ParseErrorException pee) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    pee.getLocalizedMessage());
        } catch (IOException ioe) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    ioe.getLocalizedMessage());
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    e.getLocalizedMessage(),
                    ce);
        }

        return true;
    }

    private void collectFields(TypeElement elem, Map<String, FieldModel> fields, boolean recurse) {
        TypeMirror type = elem.asType();

        if (recurse) {
            if ((elem.getKind() == ElementKind.CLASS)) {
                if (type.getKind() == TypeKind.DECLARED) {
                    if (elem.getSuperclass() != null && elem.getSuperclass().getKind() == TypeKind.DECLARED) {
                        DeclaredType superType = (DeclaredType) elem.getSuperclass();
                        TypeElement superElem = (TypeElement) superType.asElement();
                        if (superElem != null) {
                            collectFields(superElem, fields, recurse);
                        }
                    }
                }
            }
        }

        for (Element ec : elem.getEnclosedElements()) {
            if (ec.getKind() == ElementKind.FIELD) {

                VariableElement fe = (VariableElement) ec;

                DeltaField fea = fe.getAnnotation(DeltaField.class);

                if (fea != null) {
                    if (fea.ignore()) {
                        printNote("ignoring annotated field: " + fe.toString(), elem);
                        continue;
                    }
                }

                FieldModel field = createFieldModel(fe, fea);

                fields.put(field.name, field);

                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "annotated field: " + field.name + " // field type: " + field.type, elem);


            }
        }

    }

    /**
     * Figure out which model to use
     *
     * @param ve
     * @param dfa
     * @return
     */
    static final String MAP_CLASS = HashMap.class.getCanonicalName();

    private FieldModel createFieldModel(VariableElement ve, DeltaField dfa) {
        FieldModel res;


        if (StringUtils.startsWith(ve.asType().toString(), MAP_CLASS)) {

            MapFieldModel mapRes = new MapFieldModel();

            DeclaredType ty = (DeclaredType) ve.asType();

            mapRes.mapItem = dfa.mapItem();

            mapRes.key = new FieldModel();

            mapRes.key.type = "Object";
            mapRes.value = new FieldModel();
            mapRes.value.type = "Object";
            mapRes.type = ve.asType().toString();
            mapRes.boxedType = mapRes.type;

            // if we got T params
            List<? extends TypeMirror> Targs = ty.getTypeArguments();
            if (Targs.size() == 2) {
                mapRes.key = box(mapRes.key, Targs.get(0));
                mapRes.value = box(mapRes.value, Targs.get(1));
            } else {
                printWarn("Got an irregular number of type args in Map defi, ignoring", ve);
            }

            res = mapRes;


        } else {
            res = new FieldModel();
            res = box(res, ve.asType());
        }

        res.accessible = !ve.getModifiers().contains(Modifier.PRIVATE);

        res.name = ve.getSimpleName().toString();

        printNote("created field model:" + res.toString(), ve);

        return res;
    }


    /**
     * Actually writes the model
     *
     * @param model
     * @param fields
     * @throws Exception
     */
    protected void writeBuilder(DeltaBuilderTypeModel model, Map<String, FieldModel> fields) throws Exception {


        VelocityContext vc = new VelocityContext();

        vc.put("generatorClassName", this.getClass().toString());
        vc.put("model", model);
        vc.put("fields", fields);
        vc.put("date", new Date().toString());

        // adding DisplayTool from Velocity Tools library
        vc.put("display", new DisplayTool());

        Template vt = velocityEngine.getTemplate("DeltaBuilder.vm");

        JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                model.getDeltaBuilderClassName());

        printNote("creating source file: " + jfo.toUri(), null);

        Writer writer = jfo.openWriter();

        printNote("applying velocity template: " + vt.getName(), null);

        vt.merge(vc, writer);

        writer.close();
    }

    private void printError(String s, Element e) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.ERROR, s, e);

    }

    private void printNote(String msg, Element elem) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE, msg, elem);
    }

    private void printWarn(String s, Element elem) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.WARNING, s, elem);
    }


    private FieldModel box(FieldModel field, TypeMirror typeMirror) {

        field.type = typeMirror.toString();

        if (typeMirror.getKind().isPrimitive()) {
            field.primitive = true;

            printNote("boxing:" + typeMirror.getKind().name(), null);

            field.boxedType = autobox(typeMirror).getName();

        } else {
            field.boxedType = field.type;

            field.primitive = true;

        }
        return field;

    }

    public Class autobox(TypeMirror mirror) {

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
                    printWarn("Failed to autobox from " + mirror, null);
                    return Object.class;
                }
        }
    }
}
