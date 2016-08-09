package org.namstorm.deltaforce.samples.ledgers;

import org.namstorm.deltaforce.ledgers.LedgerSchemaImpl;
import org.namstorm.deltaforce.samples.ComplexPerson;
import org.namstorm.deltaforce.samples.ledgers.model.Order;

import java.util.List;

/**
 * Created by maxnamstorm on 9/8/2016.
 */
public class SampleLedgerSchema extends LedgerSchemaImpl {
    ComplexPerson buyer;
    ComplexPerson salesPerson;

    public ComplexPerson getBuyer() {
        return buyer;
    }

    public ComplexPerson getSalesPerson() {
        return salesPerson;
    }

    public List<Order> getOrders() {
        return orders;
    }

    List<Order> orders;

}
