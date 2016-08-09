package org.namstorm.deltaforce.samples.ledgers.model;

import org.namstorm.deltaforce.annotations.DeltaBuilder;

import java.util.Date;
import java.util.UUID;

/**
 * Created by maxnamstorm on 9/8/2016.
 */
@DeltaBuilder
public class Order {
    Date dateTime;

    public UUID getProductId() {
        return productId;
    }

    UUID productId;

    public Date getDateTime() {
        return dateTime;
    }
}
