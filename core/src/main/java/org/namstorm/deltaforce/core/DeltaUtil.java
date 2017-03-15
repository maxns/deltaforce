package org.namstorm.deltaforce.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by maxnamstorm on 6/8/2016.
 */
public class DeltaUtil {


    public static class ApplyToMap extends RecursiveDeltaMapVisitor {

        @Override
        protected void visitMap(DeltaMap d) {
            switch (d.getOp()) {
                case ADD:
                case UPDATE:
                    // we're about to go into the map, let's take care of the stack
                    toStack.push(to);
                    Map next = (Map) to.get(d.getFieldName());

                    // if nothing in the stack, need to create something
                    if(next==null) {
                        next = new HashMap();
                        to.put(d.getFieldName(), next);
                    }

                    to = next;
                    super.visitMap(d);
                    to = toStack.pop();

                    break;
                case REMOVE:
                    to.remove(d.getFieldName());
                    break;
                case NOOP:
                    break;
            }
        }

        class ApplyField implements DeltaVisitor {

            public void visit(Delta d) {
                switch (d.getOp()) {
                    case ADD:
                    case UPDATE:
                        to.put(d.getFieldName(), d.getNewValue());
                        break;
                    case REMOVE:
                        to.remove(d.getFieldName());
                        break;
                    case NOOP:
                        break;
                }
            }
        }

        private Map to;
        private final Stack<Map> toStack = new Stack<>();

        public ApplyToMap(final Map toMap) {
            super();
            super.fieldVisitor = new ApplyField();
            this.to = toMap;
        }
    }

    /**
     * Will process deltamap entries and apply them to the map, respecting various operands
     *
     * @param to
     * @param from
     * @return
     */
    public static Map applyMapDeltas(final DeltaMap from, final Map<?,?> to) {
        DeltaUtil.visitDeltas(from, new ApplyToMap(to));
        return to;
    }

    /**
     * Simply calls visitor to exam
     *
     * @param deltaMap
     * @param visitor
     */
    public static void visitDeltas(final DeltaMap deltaMap, final DeltaVisitor visitor) {
        deltaMap.map().values().forEach(o -> visitor.visit((Delta<?>) o));

    }


}
