package org.namstorm.deltaforce.annotations.processors.util;

import org.namstorm.fluency.OnResult;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by maxnam-storm on 15/8/2016.
 */
public class DFProcessorUtil {
    public static class OnAnnotationResult<T extends Annotation,R> extends OnResult<T[]> {
        public interface Selector<T, R> {
            public R select(T from);
        }

        R defaultValue;

        public OnAnnotationResult(T[] result, R defaultValue) {
            super(result);
            this.defaultValue = defaultValue;
        }

        public R select(Selector<T,R> c) {
            if(result!=null && result.length>0) {
                R res;
                for (T a:result) {
                    res = c.select(a);
                    if(res!=null) return res;
                }
            }
            return defaultValue;

        }

    }
    /**
     * if the annotation exists on the element, ask the consumer to consume
     *
     * @param element
     * @param annotClass
     *
     * @return
     */
    public static <E extends Annotation, D> OnAnnotationResult<E, D> onAnnotations(Element element, Class<E> annotClass, D defaultValue) {

        return new OnAnnotationResult<>(element.getAnnotationsByType(annotClass), defaultValue);
    }
}
