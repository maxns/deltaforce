package org.namstorm.deltaforce.benchmark;

import java.io.InvalidObjectException;
import java.util.concurrent.TimeUnit;

import org.namstorm.deltaforce.samples.ClientOrder;
import org.namstorm.deltaforce.samples.ClientOrderBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@SuppressWarnings("javadoc")
@State(Scope.Benchmark)
public class ClientOrderBenchmark {

    private ClientOrder[] orders;
    private int index = 0;

    @Setup(Level.Trial)
    public void setUp() {
        orders = new ClientOrder[10000];
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        orders = null;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void latency() throws InvalidObjectException {
        ClientOrderBuilder order = new ClientOrderBuilder();
        order.setClientId(1234);
        order.setAssignee("ALGO");
        order.setBoardCode("MAIN");
        order.setCurrencyCode("HKD");
        order.setQuantity(1000);
        order.setId(123456789);
        order.setPrice(3.0);
        order.setCreatedBy("CREATEDBY");
        order.setUpdatedBy("UPDATEDBY");
        order.setExchangeCode("1234");
        order.setFixOrder(true);
        order.setCreatedTime(System.currentTimeMillis());
        order.setOrderStatus((byte) 'a');
        order.setSide((byte) '1');

        order.apply();
        orders[(index++) % orders.length] = order;
    }

}