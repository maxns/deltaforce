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


    /**
     * Default 'Builder' - but can be set to anything as suffix of the builders alias
     * @return
     */
    String builderNameSuffix() default "Builder";

    /**
     * If set to true, will NOT go into superclass and get fields from it
     * @return
     */
    boolean ignoreInherited() default false;

}
