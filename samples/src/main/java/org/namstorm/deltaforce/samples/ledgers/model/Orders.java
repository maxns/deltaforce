package org.namstorm.deltaforce.samples.ledgers.model;

import org.namstorm.deltaforce.annotations.DeltaBuilder;
import org.namstorm.deltaforce.annotations.DeltaField;
import org.namstorm.deltaforce.core.CollectionWrapper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by maxnamstorm on 14/8/2016.
 */
@DeltaBuilder
public class Orders extends CollectionWrapper<Order>{

    @DeltaField(alias="-s")
    private Collection<Order> orders = new ArrayList<>();

    public Orders() {
        super();
    }

    @Override
    protected Collection<Order> items() {
        return orders;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }
}
