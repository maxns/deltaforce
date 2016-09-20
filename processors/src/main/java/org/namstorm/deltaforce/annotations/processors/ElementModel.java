package org.namstorm.deltaforce.annotations.processors;

import javax.lang.model.element.Element;

/**
 * Created by maxnamstorm on 10/8/2016.
 *
 */
public interface ElementModel<T extends Element> {

    /**
     *
     * @return
     */
    boolean getAccessible();

    /**
     *
     * @return
     */
    String getAccessor();

    /**
     * whether there is a setter defined.  If not, then it will not generate an
     * @Override for the setter.
     *
     * @return
     */
    boolean getHasSetter();

    /**
     *
     * @return
     */
    String getType();

    /**
     *
     * @return
     */
    String getName();
}
