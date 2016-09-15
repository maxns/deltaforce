package org.namstorm.deltaforce.samples;

import org.namstorm.deltaforce.annotations.DeltaForceBuilder;

/**
 * Trade
 */
@DeltaForceBuilder
interface Trade {

    String getId();

    void setId(String id);

    long getCreatedTime();

    void setCreatedTime(long createdTime);

    long getUpdatedTime();

    void setUpdatedTime(long updatedTime);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);

    byte getTradeStatus();

    void setTradeStatus(byte tradeStatus);

    byte getSide();

    void setSide(byte side);

    double getPrice();

    void setPrice(double price);

    long getClientId();

    void setClientId(long clientId);

    String getLocalCode();

    void setLocalCode(String localCode);

    String getExchangeCode();

    void setExchangeCode(String exchangeCode);

}
