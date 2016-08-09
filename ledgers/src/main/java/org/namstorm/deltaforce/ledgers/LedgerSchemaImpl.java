package org.namstorm.deltaforce.ledgers;

import java.util.Set;

/**
 * Created by maxnam-storm on 9/8/2016.
 */
public class LedgerSchemaImpl<T extends LedgerField> implements LedgerSchema<T> {


    private Set<T> fields;

    @Override
    public Set<T> fields() {
        return fields;
    }
}
