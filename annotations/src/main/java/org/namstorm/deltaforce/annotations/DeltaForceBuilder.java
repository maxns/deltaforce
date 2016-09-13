package org.namstorm.deltaforce.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The DeltaForceBuilder annotation type, marks a class to be enabled for DeltaForceBuilder
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
public @interface DeltaForceBuilder {


    /**
     * Default 'Builder' - but can be set to anything as suffix of the builders alias
     *
     */
    String builderNameSuffix() default "Builder";

    /**
     * If set to true, will NOT go into superclass and get fields from it
     *
     */
    boolean ignoreInherited() default false;

    /**
     * sets up a custom base for the builder
     *
     */
    String extend() default "org.namstorm.deltaforce.core.AbstractDeltaBuilder";

    /**
     * sets up interfaces to implement
     *
     */
    String[] implement() default {};

}
