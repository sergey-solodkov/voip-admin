package com.github.sergeisolodkov.voipadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceModelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DeviceModel getDeviceModelSample1() {
        return new DeviceModel()
            .id(1L)
            .name("name1")
            .linesAmount(1)
            .configTemplatePath("configTemplatePath1")
            .firmwareFilePath("firmwareFilePath1");
    }

    public static DeviceModel getDeviceModelSample2() {
        return new DeviceModel()
            .id(2L)
            .name("name2")
            .linesAmount(2)
            .configTemplatePath("configTemplatePath2")
            .firmwareFilePath("firmwareFilePath2");
    }

    public static DeviceModel getDeviceModelRandomSampleGenerator() {
        return new DeviceModel()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .linesAmount(intCount.incrementAndGet())
            .configTemplatePath(UUID.randomUUID().toString())
            .firmwareFilePath(UUID.randomUUID().toString());
    }
}
