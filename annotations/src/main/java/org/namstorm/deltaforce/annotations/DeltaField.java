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

    /**
     * Supply this for map fields - to provide a logical-sounding name for map accessors
     * e.g.
     *
     * @return
     * @DataField(mapItem="metaData") Map metaDataMap;
     * <p>
     * If not provided, the builder will call them set{FieldName>}Value
     */
    String mapItem() default "";
}
