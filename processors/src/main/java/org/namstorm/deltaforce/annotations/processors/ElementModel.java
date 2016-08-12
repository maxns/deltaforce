package org.namstorm.deltaforce.annotations.processors;

import javax.lang.model.element.Element;

/**
 * Created by maxnamstorm on 10/8/2016.
 *
 */
public interface ElementModel<T extends Element> {

    boolean getAccessible();

    String getType();

    String getName();
}
