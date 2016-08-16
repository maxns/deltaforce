package org.namstorm.deltaforce.core;

/**
 * Created by maxnam-storm on 9/8/2016.
 *
 * T is the underlying object this thing knows how to build
 */
public interface DeltaBuilder<T extends Object> {

    /**
     * Sets the "root" op - ie what is happening to the root objects (not just its fields)
     */
    DeltaBuilder op(Delta.OP op);

    /**
     *
     * Will visit deltas
     * @param visitor
     */
    void visitDeltas(DeltaVisitor visitor);

    T build();

    /**
     * applies deltas to an object to
     *
     * @param to object to which to apply this
     * @return from
     */
    T apply(T to);

    /**
     * creates a new object
     *
     * @see T .build()
     * @see T .apply(T)
     *
     * @return
     */
    T create();


    /**
     * Starting point for detlas
     *
     * @return
     */
    DeltaBuilder<T> from(T from);
}
