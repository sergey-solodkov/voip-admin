package com.github.sergeisolodkov.voipadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SettingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Setting getSettingSample1() {
        return new Setting().id(1L).textValue("textValue1");
    }

    public static Setting getSettingSample2() {
        return new Setting().id(2L).textValue("textValue2");
    }

    public static Setting getSettingRandomSampleGenerator() {
        return new Setting().id(longCount.incrementAndGet()).textValue(UUID.randomUUID().toString());
    }
}
