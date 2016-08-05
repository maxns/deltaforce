package org.namstorm.deltaforce.annotations;

import java.lang.annotation.*;

/**
 * The DeltaBuilder annotation type, marks a class to be enabled for DeltaBuilder
 *
 * @author maxns
 * @version 1.0
 */
@Documented
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD
})
@Retention(RetentionPolicy.SOURCE)
public @interface DeltaBuilder {


    String builderNameSuffix() default "Builder";


}