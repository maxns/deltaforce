package org.namstorm.deltaforce.samples.ledgers;

import org.namstorm.deltaforce.ledgers.LedgerFieldImpl;
import org.namstorm.deltaforce.samples.ComplexPerson;
import org.namstorm.deltaforce.samples.ComplexPersonBuilder;
import org.namstorm.deltaforce.samples.ledgers.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxnam-storm on 9/8/2016.
 */
public class SampleLedger extends SerializingDeltaLedger<SampleLedger.Schema> {

    static class Schema extends SampleLedgerSchema {
        private transient ComplexPersonBuilder buyerBuilder;
        private transient ComplexPersonBuilder salesPersonBuilder;
    }

    public ComplexPerson buyer() { return schema().buyer; }
    public ComplexPerson salesPerson() { return schema().buyer; }

    public ComplexPersonBuilder editBuyer() {
        _assertOpen("Can't edit until ledger is open");
        return schema().buyerBuilder.from(schema().buyer);
    }
    public ComplexPersonBuilder editSalesPerson() {
        _assertOpen("Can't edit until ledger is open");
        return schema().salesPersonBuilder.from(schema().salesPerson);
    }


    @Override
    protected Schema initSchema() {

        Schema res = new Schema();
        res.buyer  = new ComplexPerson();
        res.buyerBuilder = new ComplexPersonBuilder(res.buyer);

        res.salesPerson = new ComplexPerson();
        res.salesPersonBuilder = new ComplexPersonBuilder(res.salesPerson);

        res.orders = new ArrayList<>();

        res.addField(new LedgerFieldImpl<ComplexPerson>(res.buyer.getClass(), "buyer", res.buyer, res.buyerBuilder));
        res.addField(new LedgerFieldImpl<ComplexPerson>(res.salesPerson.getClass(), "salesPerson", res.salesPerson, res.salesPersonBuilder));
        //res.addField(new LedgerFieldImpl<List<Order>>(res.orders.getClass(), "orders", res.orders, null);

        return res;
    }

}
