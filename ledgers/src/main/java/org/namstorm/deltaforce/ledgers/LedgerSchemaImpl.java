package org.namstorm.deltaforce.ledgers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by maxnam-storm on 9/8/2016.
 */
public class LedgerSchemaImpl<T extends LedgerField> implements LedgerSchema<T> {


    private final Set<T> fields;
    private final Set<T> fields_;

    public LedgerSchemaImpl() {
        super();
        fields = new HashSet<>();
        fields_ = Collections.unmodifiableSet(fields);
    }

    @Override
    public Set<T> fields() {
        return fields_;
    }

    public void addField(T f) {
        fields.add(f);
    }
}
