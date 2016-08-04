package org.namstorm.deltaforce.annotations.processors;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.DisplayTool;

import org.namstorm.deltaforce.annotations.DeltaBuilder;

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

    /** String used to append to a class name when creating the DeltaBuilder class name. */
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
     * @param roundEnv the environment for this processors round
     *
     * @return whether a new processors round would be needed
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (annotations.isEmpty()) {
            return true;
        }

        try {
            DeltaBuilderTypeModel model = null;
            Map<String, FieldModel> fields = new HashMap<>();
            Map<String, DeltaBuilderSetterMethodModel> methods = new HashMap<>();

            for (Element e : roundEnv.getElementsAnnotatedWith(DeltaBuilder.class)) {

                if (e.getKind() == ElementKind.CLASS) {

                    model = new DeltaBuilderTypeModel();

                    TypeElement classElement = (TypeElement) e;
                    PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
                    DeltaBuilder annotation = classElement.getAnnotation(DeltaBuilder.class);

                    model.packageName = packageElement.getQualifiedName().toString();
                    model.className = classElement.getSimpleName().toString();
                    model.qualifiedName = classElement.getQualifiedName().toString();

                    processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "annotated class: " + model.qualifiedName, e);

                } else if (e.getKind() == ElementKind.FIELD) {

                    VariableElement varElement = (VariableElement) e;

                    FieldModel field = new FieldModel();
                    DeltaBuilder annotation = varElement.getAnnotation(DeltaBuilder.class);

                    field.name = varElement.getSimpleName().toString();
                    field.type = varElement.asType().toString();

                    fields.put(field.name, field);

                    processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "annotated field: " + field.name + " // field type: " + field.type, e);

                } else if (e.getKind() == ElementKind.METHOD) {

                    ExecutableElement exeElement = (ExecutableElement) e;
                    List<? extends VariableElement> paramElements = exeElement.getParameters();
                    DeltaBuilder annotation = exeElement.getAnnotation(DeltaBuilder.class);

                    DeltaBuilderSetterMethodModel method = new DeltaBuilderSetterMethodModel();
                    List<FieldModel> parameters = new ArrayList<>();

                    method.name = exeElement.getSimpleName().toString();
                    method.qualifiedType = exeElement.getReturnType().toString();

                    for (VariableElement paramElement : paramElements) {
                        FieldModel parameter = new FieldModel();
                        DeltaBuilder paramAnnotation = paramElement.getAnnotation(DeltaBuilder.class);

                        parameter.name = paramElement.getSimpleName().toString();
                        parameter.type = paramElement.asType().toString();

                        parameters.add(parameter);
                    }

                    methods.put(method.name, method);

                    processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "annotated method: " + method.name + " // return type: " + method.qualifiedType, e);
                    for (FieldModel parameter : parameters) {
                        processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.NOTE,
                            "parameter: " + parameter.name + " // parameter type: " + parameter.type, e);
                    }
                }
            }

            if (model != null) {

                Properties props = new Properties();
                URL url = this.getClass().getClassLoader().getResource("velocity.properties");
                props.load(url.openStream());

                VelocityEngine ve = new VelocityEngine(props);
                ve.init();

                VelocityContext vc = new VelocityContext();

                vc.put("model", model);
                vc.put("fields", fields);
                vc.put("methods", methods);

                // adding DisplayTool from Velocity Tools library
                vc.put("display", new DisplayTool());

                Template vt = ve.getTemplate("DeltaBuilder.vm");

                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                    model.qualifiedName);

                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "creating source file: " + jfo.toUri());

                Writer writer = jfo.openWriter();

                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "applying velocity template: " + vt.getName());

                vt.merge(vc, writer);

                writer.close();
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
                e.getLocalizedMessage());
        }

        return true;
    }
}
