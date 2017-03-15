package org.namstorm.deltaforce.annotations.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * MethodModelBuilderFactory
 */
class MethodModelBuilderFactory {

    private static final MethodModelBuilderFactory _instance = new MethodModelBuilderFactory();

    static MethodModelBuilderFactory getInstance() {
        return _instance;
    }

    /**
     *
     * @param pe
     * @param e
     * @return
     */
    public MethodModelBuilder create(ProcessingEnvironment pe, Element e)  {
        return new MethodModelBuilder(pe).with(e);
    }

}
