package org.namstorm.deltaforce.annotations.processors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.DisplayTool;
import org.namstorm.deltaforce.annotations.DeltaBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Annotation processors for DeltaBuilder annotation type. It generates a full featured
 * DeltaBuilder type with the help of an Apache Velocity template.
 *
 * @author deors
 * @version 1.0
 */
@SupportedAnnotationTypes("org.namstorm.deltaforce.annotations.DeltaBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DeltaBuilderProcessor
        extends AbstractProcessor {

    /**
     * String used to append to a class name when creating the DeltaBuilder class name.
     */
    private static final String BEAN_INFO = "DeltaBuilder";

    /**
     * Default constructor.
     */
    public DeltaBuilderProcessor() {

        super();
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
                            "annotated class: " + model.qualifiedName, elem);


                    for (Element ec : elem.getEnclosedElements()) {
                        if (ec.getKind() == ElementKind.FIELD) {

                            VariableElement fe = (VariableElement) ec;
                            ce = fe;

                            FieldModel field = new FieldModel();
                            DeltaField fea = fe.getAnnotation(DeltaField.class);

                            if (fea == null || fea.ignore()) {
                                field.accessible = !fe.getModifiers().contains(Modifier.PRIVATE);

                                field.name = fe.getSimpleName().toString();
                                field.type = fe.asType().toString();

                                if (fe.asType().getKind().isPrimitive()) {
                                    field.boxedType = box(fe.asType());
                                    field.primitive = true;
                                } else {
                                    field.boxedType = field.type;
                                    field.primitive = false;
                                }

                                fields.put(field.name, field);

                                processingEnv.getMessager().printMessage(
                                        Diagnostic.Kind.NOTE,
                                        "annotated field: " + field.name + " // field type: " + field.type, elem);
                            } else {
                                processingEnv.getMessager().printMessage(
                                        Diagnostic.Kind.NOTE,
                                        "IGNORING annotated field: " + field.name + " // field type: " + field.type, elem);

                            }

                        }
                    }


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

    protected void writeBuilder(DeltaBuilderTypeModel model, Map<String, FieldModel> fields) throws Exception {

        Properties props = new Properties();
        URL url = this.getClass().getClassLoader().getResource("velocity.properties");
        props.load(url.openStream());

        VelocityEngine ve = new VelocityEngine(props);
        ve.init();

        VelocityContext vc = new VelocityContext();

        vc.put("generatorClassName", this.getClass().toString());
        vc.put("model", model);
        vc.put("fields", fields);

        // adding DisplayTool from Velocity Tools library
        vc.put("display", new DisplayTool());

        Template vt = ve.getTemplate("DeltaBuilder.vm");

        JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                model.getDeltaBuilderClassName());

        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE,
                "creating source file: " + jfo.toUri());

        Writer writer = jfo.openWriter();

        printNote("applying velocity template: " + vt.getName());

        vt.merge(vc, writer);

        writer.close();
    }

    private void printNote(String msg) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE, msg);
    }


    private String box(TypeMirror typeMirror) {


        if (typeMirror.getKind().isPrimitive()) {
            printNote("boxing:" + typeMirror.getKind().name());
            Class res = autobox(typeMirror.getKind());

            return res == null ? "Object" : res.getName();
        }

        return typeMirror.toString();

    }

    public static Class autobox(TypeKind kind) {

        switch (kind) {
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
                return null;
        }
    }
}
