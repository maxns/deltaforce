package org.namstorm.deltaforce.ledgers;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections.SetUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by maxnam-storm on 9/8/2016.
 */
public class LedgerSchemaImpl<T extends LedgerField> implements LedgerSchema<T> {


    private Set<T> fields;
    private Set<T> fields_;

    public LedgerSchemaImpl() {
        super();
        fields = new HashSet<T>();
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
