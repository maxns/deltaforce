package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;

/**
 * Created by maxnamstorm on 10/8/2016.
 */
public abstract class ModelBuilder<E extends Element, T extends ElementModel<E>> {
    protected E element;
    protected ProcessingEnvironment pe;

    public ModelBuilder() {
        super();
    }

    public ModelBuilder(ProcessingEnvironment processingEnvironment) {
        this.pe = processingEnvironment;
    }

    /**
     * assigns element <b>with</b> which to build
     * @param builder
     * @param element
     * @param <B>
     * @return
     */
    public <B extends VariableModelBuilder> B with(B builder, E element) {
        builder.element = element;
        return builder;
    }

    /**
     * if the annotation exists on the element, ask the consumer to consume
     *
     * @param annotClass
     * @param consumer
     * @param <A>
     * @return
     */
    protected <A extends Annotation> A whenAnnotated(Class<A> annotClass, Consumer<A> consumer) {

        A a = element.getAnnotation(annotClass);

        if(a!=null) {
            consumer.accept(a);
        }

        return a;
    }
}
