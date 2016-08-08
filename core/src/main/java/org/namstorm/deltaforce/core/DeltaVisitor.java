package org.namstorm.deltaforce.core;

/**
 * Created by maxnam-storm on 5/8/2016.
 */
public interface DeltaVisitor {
    public void visit(Delta<?> delta);
}
