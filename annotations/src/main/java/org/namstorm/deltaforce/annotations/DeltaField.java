package org.namstorm.deltaforce.annotations;

import java.lang.annotation.*;

/**
 * Created by maxnam-storm on 5/8/2016.
 */
@Documented
@Target({
        ElementType.FIELD
})
@Retention(RetentionPolicy.SOURCE)
public @interface DeltaField {
    enum Type {
        /** auto-detect */
        AUTO,
        /** access like a field */
        FIELD,
        /** access like a map (set/remove) */
        MAP,
        /** access like a collection (add/remove) */
        COLLECTION,
        /** access like a builder (FIELD + modify) */
        BUILDER
    };

    /**
     * Set this to true and this field will be ignore
     * @return
     */
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
    String alias() default "";

    /**
     * Define what kind of a field this is
     *
     * AUTO - is the default, so the framework will try to figure out itself, by examining the type
     *
     * FIELD - basic set/getters for all primitive and declared, and then have special accessors for maps and collections.
     *
     * MAP/COLLECTION - as states on the box, 'set / remove' for MAP and 'add/remove' for collection
     *
     * BUILDER - is like auto + creates a "editXXX" field that actually returns the builder
     *
     * @return
     */
    Type type() default Type.AUTO;
}
