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
