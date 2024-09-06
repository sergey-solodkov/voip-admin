package com.github.sergeisolodkov.voipadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OtherDeviceTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OtherDeviceType getOtherDeviceTypeSample1() {
        return new OtherDeviceType().id(1L).name("name1").description("description1");
    }

    public static OtherDeviceType getOtherDeviceTypeSample2() {
        return new OtherDeviceType().id(2L).name("name2").description("description2");
    }

    public static OtherDeviceType getOtherDeviceTypeRandomSampleGenerator() {
        return new OtherDeviceType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
