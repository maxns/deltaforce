package org.namstorm.deltaforce.annotations.processors;

import javax.lang.model.element.VariableElement;

/**
 * Created by maxnam-storm on 10/8/2016.
 */
public interface FieldModel extends ElementModel<VariableElement>{
    String getAccessorType();

    String getBoxedType();

    String getAlias();

    String getClassName();
}
