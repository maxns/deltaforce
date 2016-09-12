package org.namstorm.deltaforce.annotations.processors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.DisplayTool;
import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.annotations.DeltaForceBuilder;
import org.namstorm.deltaforce.annotations.processors.util.DFUtil;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * Annotation processors for DeltaForceBuilder annotation type. It generates a full featured
 * DeltaForceBuilder type with the help of an Apache Velocity template.
 *
 * @version 1.0
 */
public class DeltaBuilderProcessor
        extends AbstractProcessor {

    /**
     * keeping it plain, avoiding Android issues
     */
    static HashSet<String> SUPPORTED_TYPES = new HashSet<>(Arrays.asList("org.namstorm.deltaforce.annotations.DeltaForceBuilder"));
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
     * Reads the DeltaForceBuilder information and writes a full featured
     * DeltaForceBuilder type with the help of an Apache Velocity template.
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
            for (Element elem : roundEnv.getElementsAnnotatedWith(DeltaForceBuilder.class)) {
                ce = elem;

                if (elem.getKind() == ElementKind.CLASS) {
                    createBuilderModel(modelMap, elem);
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

    private void createBuilderModel(HashMap<DeltaBuilderTypeModel, Map<String, FieldModel>> modelMap, Element elem) {
        DeltaBuilderTypeModel model;
        model = new DeltaBuilderTypeModel();


        Map<String, FieldModel> fields = new HashMap<>();
        modelMap.put(model, fields);

        TypeElement classElement = (TypeElement) elem;
        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();

        DeltaForceBuilder annotation = classElement.getAnnotation(DeltaForceBuilder.class);

        model.packageName = packageElement.getQualifiedName().toString();
        model.className = classElement.getSimpleName().toString();
        model.qualifiedName = classElement.getQualifiedName().toString();
        model.deltaBuilderClassName = model.className + annotation.builderNameSuffix();
        model.deltaBuilderQualifiedName = model.packageName + "." + model.deltaBuilderClassName;
        model.extendClassName = annotation.extend() + "<" + model.className + ">";
        model.implementsInterfaces = "";

        for (int i = 0; i < annotation.implement().length; i++) {
            model.implementsInterfaces = model.implementsInterfaces + (i>0?",":"") +
                    annotation.implement()[i];
        }
        model.implementsInterfaces = model.implementsInterfaces.replace("/<@>/g","<" + model.className + ">");

        // see if this is a special buildable guy


        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE,
                "annotating class: " + model.qualifiedName, elem);

        collectFields(classElement, fields, !annotation.ignoreInherited());
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

                FieldModel field = null;
                try {
                    field = createFieldModel(fe, fea);

                    fields.put(field.getName(), field);

                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.NOTE,
                            "annotated field: " + field.getName() + " // field type: " + field.getType(), elem);

                } catch (Exception e) {
                    printError("Failed to create field:" + e, fe);
                    e.printStackTrace();
                }



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

    private FieldModel createFieldModel(VariableElement ve, DeltaField dfa) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        return FieldModelBuilderFactory.getInstance().create(processingEnv, ve).build();
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
        vc.put("util", DFUtil.INSTANCE);

        // adding DisplayTool from Velocity Tools library
        vc.put("display", new DisplayTool());

        Template vt = velocityEngine.getTemplate("DeltaBuilder.vm");

        JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                model.getDeltaBuilderQualifiedName());


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



}
