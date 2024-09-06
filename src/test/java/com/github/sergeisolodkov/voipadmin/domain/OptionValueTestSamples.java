package com.github.sergeisolodkov.voipadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OptionValueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OptionValue getOptionValueSample1() {
        return new OptionValue().id(1L).value("value1");
    }

    public static OptionValue getOptionValueSample2() {
        return new OptionValue().id(2L).value("value2");
    }

    public static OptionValue getOptionValueRandomSampleGenerator() {
        return new OptionValue().id(longCount.incrementAndGet()).value(UUID.randomUUID().toString());
    }
}
