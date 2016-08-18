package org.namstorm.deltaforce.samples.ledgers.model;

import org.namstorm.deltaforce.annotations.DeltaForceBuilder;

import java.util.Date;
import java.util.UUID;

/**
 * Created by maxnamstorm on 9/8/2016.
 */
@DeltaForceBuilder
public class Order {
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;

    public Order(){
        super();
    }
    public Order(UUID productId, String description) {
        super();
        this.productId = productId;
        this.description = description;
    }

    Date dateTime;

    public UUID getProductId() {
        return productId;
    }

    UUID productId;

    public Date getDateTime() {
        return dateTime;
    }
}
