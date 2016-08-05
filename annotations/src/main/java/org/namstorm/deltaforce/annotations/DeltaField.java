package org.namstorm.deltaforce.annotations;

import java.lang.annotation.*;

/**
 * Created by maxnam-storm on 5/8/2016.
 */
@Documented
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD
})
@Retention(RetentionPolicy.SOURCE)
public @interface DeltaField {
    boolean ignore() default false;
}
