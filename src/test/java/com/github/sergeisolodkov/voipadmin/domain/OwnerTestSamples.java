package com.github.sergeisolodkov.voipadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OwnerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Owner getOwnerSample1() {
        return new Owner()
            .id(1L)
            .code("code1")
            .firstName("firstName1")
            .secondName("secondName1")
            .lastName("lastName1")
            .position("position1")
            .location("location1");
    }

    public static Owner getOwnerSample2() {
        return new Owner()
            .id(2L)
            .code("code2")
            .firstName("firstName2")
            .secondName("secondName2")
            .lastName("lastName2")
            .position("position2")
            .location("location2");
    }

    public static Owner getOwnerRandomSampleGenerator() {
        return new Owner()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .secondName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString());
    }
}
