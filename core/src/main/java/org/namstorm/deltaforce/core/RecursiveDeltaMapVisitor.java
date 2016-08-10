package org.namstorm.deltaforce.core;

/**
 * Created by maxnamstorm on 6/8/2016.
 *
 * This thing knows how to go recursive into DeltaMap
 */
public class RecursiveDeltaMapVisitor implements DeltaVisitor{
    protected DeltaVisitor fieldVisitor;

    public RecursiveDeltaMapVisitor(DeltaVisitor fieldVisitor) {
        this.fieldVisitor = fieldVisitor;
    }

    protected RecursiveDeltaMapVisitor() {
        this.fieldVisitor = f -> {};
    }

    @Override
    public void visit(Delta<?> delta) {


        if(delta instanceof DeltaMap) {
            visitMap((DeltaMap)delta);
        }else {
            visitField(delta);
        }
    }

    protected void visitField(Delta<?> delta) {
        fieldVisitor.visit(delta);
    }

    protected void visitMap(DeltaMap<String, Delta> deltaMap) {
        deltaMap.values().forEach(d -> visit(d));
    }
}
