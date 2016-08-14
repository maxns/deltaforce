package org.namstorm.deltaforce.samples.ledgers;

import org.namstorm.deltaforce.ledgers.LedgerFieldImpl;
import org.namstorm.deltaforce.samples.ComplexPerson;
import org.namstorm.deltaforce.samples.ComplexPersonBuilder;
import org.namstorm.deltaforce.samples.ledgers.model.Order;
import org.namstorm.deltaforce.samples.ledgers.model.Orders;
import org.namstorm.deltaforce.samples.ledgers.model.OrdersBuilder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by maxnam-storm on 9/8/2016.
 */
public class SampleLedger extends SerializingDeltaLedger<SampleLedger.Schema> {

    static class Schema extends SampleLedgerSchema {
        private transient ComplexPersonBuilder buyerBuilder;
        private transient ComplexPersonBuilder salesPersonBuilder;
        public transient OrdersBuilder ordersBuilder;
    }

    public ComplexPerson buyer() { return schema().buyer; }
    public ComplexPerson salesPerson() { return schema().salesPerson; }
    public Orders orders() { return schema().orders; }

    public ComplexPersonBuilder editBuyer() {
        _assertOpen("Can't edit until ledger is open");
        return schema().buyerBuilder.from( schema().buyer );
    }
    public ComplexPersonBuilder editSalesPerson() {
        _assertOpen("Can't edit until ledger is open");
        return schema().salesPersonBuilder.from( schema().salesPerson );
    }

    public OrdersBuilder editOrders() {
        _assertOpen("Can't edit until ledger is open");
        return schema().ordersBuilder.from( schema().orders );
    }



    @Override
    protected Schema initSchema() {

        Schema res = new Schema();

        res.buyer  = new ComplexPerson();
        res.buyerBuilder = new ComplexPersonBuilder(res.buyer);

        res.salesPerson = new ComplexPerson();
        res.salesPersonBuilder = new ComplexPersonBuilder(res.salesPerson);

        res.orders = new Orders();
        res.ordersBuilder = new OrdersBuilder(res.orders);

        res.addField(new LedgerFieldImpl<ComplexPerson>(res.buyer.getClass(), "buyer", res.buyer, res.buyerBuilder));
        res.addField(new LedgerFieldImpl<ComplexPerson>(res.salesPerson.getClass(), "salesPerson", res.salesPerson, res.salesPersonBuilder));
        res.addField(new LedgerFieldImpl<Orders>(res.orders.getClass(), "orders", res.orders, res.ordersBuilder));

        return res;
    }

}
