package org.namstorm.deltaforce.annotations.processors;

import java.io.Writer;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DisplayTool;
import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.annotations.DeltaForceBuilder;
import org.namstorm.deltaforce.annotations.processors.util.DFUtil;

/**
 * Annotation processors for DeltaForceBuilder annotation type. It generates a full featured
 * DeltaForceBuilder type with the help of an Apache Velocity template.
 *
 * @version 1.0
 */
public class DeltaBuilderProcessor
        extends AbstractProcessor {

    private static final String IMPL = "Impl";

    private static final Pattern GETTER_PATTERN = Pattern.compile("^get([a-zA-Z0-9]+)$");

    private static final Pattern SETTER_PATTERN = Pattern.compile("^set([a-zA-Z0-9]+)$");

    private static final Pattern IS_PATTERN = Pattern.compile("^is([a-zA-Z0-9]+)$");

    /**
     * keeping it plain, avoiding Android issues
     */
    static HashSet<String> SUPPORTED_TYPES = new HashSet<>(Collections.singletonList("org.namstorm.deltaforce.annotations.DeltaForceBuilder"));
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
            Properties properties = new Properties();
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
        HashMap<DeltaBuilderTypeModel, Map<String, FieldModel>> interfaceModelMap = new HashMap<>();

        try {
            for (Element elem : roundEnv.getElementsAnnotatedWith(DeltaForceBuilder.class)) {
                ce = elem;

                if (elem.getKind() == ElementKind.CLASS) {
                    createBuilderModel(modelMap, elem);
                } else if (elem.getKind() == ElementKind.INTERFACE) {
                    createBuilderModel(interfaceModelMap, elem);
                }
            }

            if (modelMap.size() > 0) {
                for (DeltaBuilderTypeModel tm : modelMap.keySet()) {
                    writeBuilder(tm, modelMap.get(tm), "DeltaBuilder.vm");
                }
            }

            if (interfaceModelMap.size() > 0) {
                for (DeltaBuilderTypeModel tm : interfaceModelMap.keySet()) {
                    writeBuilder(tm, interfaceModelMap.get(tm), "InterfaceDeltaBuilder.vm");
                }
            }

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
        model.classImplName = elem.getKind() == ElementKind.INTERFACE
                ? classElement.getSimpleName().toString() + IMPL
                : classElement.getSimpleName().toString();
        model.chainSetters = elem.getKind() != ElementKind.INTERFACE;
        model.addOverrideForAccessors = elem.getKind() == ElementKind.INTERFACE;
        model.qualifiedName = classElement.getQualifiedName().toString();
        model.deltaBuilderClassName = model.className + annotation.builderNameSuffix();
        model.deltaBuilderQualifiedName = model.packageName + "." + model.deltaBuilderClassName;
        model.extendClassName = annotation.extend() + "<" + model.className + ">";
        model.implementsInterfaces = "";

        for (int i = 0; i < annotation.implement().length; i++) {
            model.implementsInterfaces = model.implementsInterfaces + (i > 0 ? "," : "") +
                    annotation.implement()[i];
        }
        model.implementsInterfaces = model.implementsInterfaces.replace("/<@>/g",
                "<" + model.className + ">");

        // see if this is a special buildable guy
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                "annotating class: " + model.qualifiedName, elem);

        if (elem.getKind() == ElementKind.INTERFACE) {
            collectMethods(classElement, fields);
        } else {
            collectFields(classElement, fields, !annotation.ignoreInherited());
        }
    }

    private void collectMethods(TypeElement elem, Map<String, FieldModel> fields) {
        Map<String, AttributeGroup> attributeGroups = new HashMap<>();

        for (Element ec : elem.getEnclosedElements()) {
            if (ec.getKind() == ElementKind.METHOD) {
                DeltaField fea = ec.getAnnotation(DeltaField.class);

                if (fea != null) {
                    if (fea.ignore()) {
                        printNote("skipping ignore=true annotated field: " + ec.toString(), elem);
                        continue;
                    }
                }

                parseAttributeGroup(ec, attributeGroups);
            }
        }

        for (Map.Entry<String, AttributeGroup> entry : attributeGroups.entrySet()) {
            AttributeGroup group = entry.getValue();
            if (group.getRepresentingElement() != null) {
                try {
                    FieldModel field = createInterfaceModel(group.getRepresentingElement(), group.setter != null);

                    fields.put(field.getName(), field);

                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                            "annotated field: " + field.getName() + " // field type: "
                                    + field.getType(),
                            elem);

                } catch (Exception e) {
                    printError("Failed to create field:" + e, group.getter);
                    e.printStackTrace();
                }
            } else if (group.setter != null) {
                throw new RuntimeException(
                        "getter for corresponding " + group.setter.toString() + " is missing");
            }
        }

    }

    private static AttributeGroup parseAttributeGroup(Element e,
            Map<String, AttributeGroup> methods) {
        AttributeGroup group = tryPattern(SETTER_PATTERN, e.getSimpleName().toString(), methods,
                g -> g.setter = e);

        if (group == null) {
            group = tryPattern(IS_PATTERN, e.getSimpleName().toString(), methods,
                    (g) -> g.getter = e);
        }

        if (group == null) {
            group = tryPattern(GETTER_PATTERN, e.getSimpleName().toString(), methods,
                    (g) -> g.getter = e);
        }

        return group;
    }

    private static AttributeGroup tryPattern(Pattern pattern, String name,
            Map<String, AttributeGroup> methods, Consumer<AttributeGroup> assign) {
        Matcher matcher = pattern.matcher(name);

        if (matcher.find()) {
            String proposedFieldName = String.valueOf(matcher.group(1).charAt(0)).toLowerCase()
                    + matcher.group(1).substring(1);

            AttributeGroup group = methods.get(proposedFieldName);
            if (group == null) {
                group = new AttributeGroup(proposedFieldName);
                methods.put(proposedFieldName, group);
            }

            assign.accept(group);
            return group;
        }

        return null;
    }

    private void collectFields(TypeElement elem, Map<String, FieldModel> fields, boolean recurse) {
        TypeMirror type = elem.asType();

        if (recurse) {
            if ((elem.getKind() == ElementKind.CLASS)) {
                if (type.getKind() == TypeKind.DECLARED) {
                    if (elem.getSuperclass() != null
                            && elem.getSuperclass().getKind() == TypeKind.DECLARED) {
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

                DeltaField fea = ec.getAnnotation(DeltaField.class);

                if (fea != null) {
                    if (fea.ignore()) {
                        printNote("ignoring annotated field: " + ec.toString(), elem);
                        continue;
                    }
                }

                try {
                    FieldModel field = createFieldModel(ec);

                    fields.put(field.getName(), field);

                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                            "annotated field: " + field.getName() + " // field type: "
                                    + field.getType(),
                            elem);

                } catch (Exception e) {
                    printError("Failed to create field:" + e, ec);
                    e.printStackTrace();
                }

            }
        }

    }

    private FieldModel createInterfaceModel(Element element, boolean hasSetter) {
        return MethodModelBuilderFactory.getInstance()
                .create(processingEnv, element)
                .setHasSetter(hasSetter)
                .build();
    }

    /**
     * Figure out which model to use
     *
     * @param element
     * @return
     */
    private FieldModel createFieldModel(Element element) {
        return FieldModelBuilderFactory.getInstance().create(processingEnv, element).build();
    }

    /**
     * Actually writes the model
     *
     * @param model
     * @param fields
     * @throws Exception
     */
    protected void writeBuilder(DeltaBuilderTypeModel model, Map<String, FieldModel> fields,
            String template) throws Exception {
        VelocityContext vc = new VelocityContext();

        vc.put("generatorClassName", this.getClass().toString());
        vc.put("model", model);
        vc.put("fields", fields);
        vc.put("date", new Date().toString());
        vc.put("util", DFUtil.INSTANCE);

        // adding DisplayTool from Velocity Tools library
        vc.put("display", new DisplayTool());

        Template vt = velocityEngine.getTemplate(template);

        JavaFileObject jfo = processingEnv.getFiler()
                .createSourceFile(model.getDeltaBuilderQualifiedName());

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

    private static final class AttributeGroup {

        private final String name;
        private Element getter;
        private Element setter;

        AttributeGroup(String name) {
            this.name = name;
        }

        Element getRepresentingElement() {
            if (getter != null) {
                return getter;
            }
            return null;
        }

    }

}
