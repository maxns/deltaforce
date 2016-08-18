package org.namstorm.deltaforce.core;

/**
 * Created by maxnam-storm on 15/8/2016.
 *
 * Just a helper interface that denotes that this thing is buildable
 *
 *
 *
 */
public interface Buildable<T, Builder extends DeltaBuilder<T>> {
    Builder getBuilder();
    void setBuilder(Builder builder);
}
