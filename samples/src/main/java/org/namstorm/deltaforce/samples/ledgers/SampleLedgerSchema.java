package org.namstorm.deltaforce.samples.ledgers;

import org.namstorm.deltaforce.ledgers.LedgerSchemaImpl;
import org.namstorm.deltaforce.samples.ComplexPerson;
import org.namstorm.deltaforce.samples.ledgers.model.Orders;

/**
 * Created by maxnamstorm on 9/8/2016.
 */
public class SampleLedgerSchema extends LedgerSchemaImpl {
    ComplexPerson buyer;
    ComplexPerson salesPerson;
    Orders orders;

    public Orders getOrders() {
        return orders;
    }


    public ComplexPerson getBuyer() {
        return buyer;
    }

    public ComplexPerson getSalesPerson() {
        return salesPerson;
    }


}
