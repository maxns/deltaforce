package org.namstorm.deltaforce.samples.ledgers;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

import org.namstorm.deltaforce.samples.ClientOrder;
import org.namstorm.deltaforce.samples.ClientOrderBuilder;

import com.google.common.base.MoreObjects;

/**
 * Created by manja on 10/16/2017.
 */
public class HeapSizeTest {

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: " + bytesToMegabytes(memory));
        printMemory();
        System.out.println("-----------------------------------------------");

        int COUNT = 500_000;
        ClientOrder[] clientOrders = new ClientOrder[COUNT];
        for (int i = 0; i < COUNT; i++) {
            ClientOrderBuilder order = new ClientOrderBuilder();
            order.setClientId(i);
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

            clientOrders[i] = order;
        }

        memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: " + bytesToMegabytes(memory));
        printMemory();
    }

    private static final long MEGABYTE = 1024L * 1024L;

    private static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    private static void printMemory() {
        MoreObjects.ToStringHelper str = MoreObjects.toStringHelper("");
        for (MemoryPoolMXBean mpBean : ManagementFactory.getMemoryPoolMXBeans()) {
            if (mpBean.getType() == MemoryType.HEAP) {
                str.add(mpBean.getName(), mpBean.getUsage());
            }
        }
        System.out.println(str.toString());
    }
}
