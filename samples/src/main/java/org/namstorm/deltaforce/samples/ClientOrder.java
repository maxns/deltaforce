package org.namstorm.deltaforce.samples;

import org.namstorm.deltaforce.annotations.DeltaForceBuilder;

/**
 * Represents a client order managed by CLOG.
 */
@DeltaForceBuilder
public interface ClientOrder {
    /**
     * This id should be globally unique.
     * <ul>
     * <li>Zero: id has not been assigned, default value.</li>
     * <li>Position value: valid id already been assigned.</li>
     * <li>Negative value: invalid!</li>
     * </ul>
     *
     * @return unique id of this order.
     */
    long getId();

    /**
     * Id of order should only be set once when order first created.
     *
     * @param value
     * @throws IllegalStateException
     *             if positive id already set.
     * @throws IllegalArgumentException
     *             if value is &lt;= 0
     */
    void setId(long value);

    /**
     * @return id of parent order; zero represents no parent order.
     */
    long getParentOrderId();

    /**
     * Sets parent order id.
     * <ul>
     * <li>Zero: no parent order id.</li>
     * <li>Position value: a valid parent order id.</li>
     * <li>Negative value: invalid!</li>
     * </ul>
     *
     * @param value
     * @throws IllegalArgumentException
     *             if value is &lt;= 0
     */
    void setParentOrderId(long value);

    /**
     * This can be assigned from {@link System#currentTimeMillis()}. Normally, it's initialized
     * while instance was first created, and it will never be changed afterwards.
     * <p>
     * The implementation should default this value to {@link System#currentTimeMillis()} while
     * creating this object.
     * </p>
     *
     * @return time when order was first created.
     */
    long getCreatedTime();

    void setCreatedTime(long value);

    /**
     * The value is as per defined as {@link System#currentTimeMillis()}.
     * <p>
     * The implementation should default this value to {@link System#currentTimeMillis()} while
     * creating this object.
     * </p>
     *
     * @return
     */
    long getUpdatedTime();

    void setUpdatedTime(long value);

    String getCreatedBy();

    void setCreatedBy(String value);

    String getUpdatedBy();

    void setUpdatedBy(String value);

    byte getOrderStatus();

    void setOrderStatus(byte value);

    String getAssignee();

    /**
     * @param value
     *            <code>null</code> value remove the assignee.
     */
    void setAssignee(String value);

    byte getSide();

    void setSide(byte value);

    double getQuantity();

    void setQuantity(double value);

    double getPrice();

    void setPrice(double value);

    String getCurrencyCode();

    void setCurrencyCode(String value);

    byte getOrderType();

    void setOrderType(byte value);

    byte getTimeInForce();

    void setTimeInForce(byte value);

    long getClientId();

    void setClientId(long value);

    byte getOrderCapacity();

    void setOrderCapacity(byte value);

    String getLocalCode();

    void setLocalCode(String value);

    String getExchangeCode();

    void setExchangeCode(String value);

    String getBoardCode();

    void setBoardCode(String value);

    boolean isFixOrder();

    void setFixOrder(boolean fixOrder);

}
