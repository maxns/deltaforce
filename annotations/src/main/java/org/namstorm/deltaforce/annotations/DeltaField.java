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
     * Supply this to override default naming behavior for fields
     *
     * If the alias is prefixed with a +, then it'll be treated as a suffix to alias
     * if the alias is prefixed with a -, then whatever follows will be discarded from the name
     * eg
     * <code>
     *     // This will make "setSurName" methods for this field
     *     @DeltaField (alias="surName")
     *     String lastName;
     *
     *     // This will make map setter calls setMetaValue
     *     @DeltaField (alias="+Value")
     *     Map&lt;String, String&gt; meta;
     *
     *     // This will make collection accessors called addItem/removeItem (without out the s)
     *     @DeltaField (alas="-s")
     *     Collection&lt;Integer&gt; items;
     *
     * </code>
     * @see @link{VeloUtil.formatAlias()}
     *
     * Note that this also affects the "getXXXX" calls on the underlying object, but does not affect
     * the direct access to fields code
     *
     */
    String alias() default "+Value";
}
