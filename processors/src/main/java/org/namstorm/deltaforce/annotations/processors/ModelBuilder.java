package org.namstorm.deltaforce.annotations.processors;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * Created by maxnamstorm on 10/8/2016.
 */
public abstract class ModelBuilder<E extends Element> {
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
     * @param element
     * @param element
     * @param <B>
     * @return
     */
    public <B extends ModelBuilder> B with(E element) {
        this.element = element;
        return (B) this;
    }

    /**
     * if the annotation exists on the element, ask the consumer to consume
     *
     * @param annotClass
     * @param consumer
     * @param <A>
     * @return
     */
    protected <A extends Annotation> A onAnnotation(Class<A> annotClass, Consumer<A> consumer) {

        A a = element.getAnnotation(annotClass);

        if(a!=null) {
            consumer.accept(a);
        }

        return a;
    }


}
