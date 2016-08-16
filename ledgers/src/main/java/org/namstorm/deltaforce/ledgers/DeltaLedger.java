package org.namstorm.deltaforce.ledgers;

/**
 * Created by maxnam-storm on 16/8/2016.
 */
public interface DeltaLedger {
    void open();

    void commit();

    void cancel();

    public enum Status {
        Open,
        Closed
    }
}
