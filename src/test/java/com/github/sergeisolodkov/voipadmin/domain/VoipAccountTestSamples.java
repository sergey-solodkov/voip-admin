package com.github.sergeisolodkov.voipadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VoipAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VoipAccount getVoipAccountSample1() {
        return new VoipAccount()
            .id(1L)
            .username("username1")
            .passwordHash("passwordHash1")
            .sipServer("sipServer1")
            .sipPort("sipPort1")
            .lineNumber(1);
    }

    public static VoipAccount getVoipAccountSample2() {
        return new VoipAccount()
            .id(2L)
            .username("username2")
            .passwordHash("passwordHash2")
            .sipServer("sipServer2")
            .sipPort("sipPort2")
            .lineNumber(2);
    }

    public static VoipAccount getVoipAccountRandomSampleGenerator() {
        return new VoipAccount()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .passwordHash(UUID.randomUUID().toString())
            .sipServer(UUID.randomUUID().toString())
            .sipPort(UUID.randomUUID().toString())
            .lineNumber(intCount.incrementAndGet());
    }
}
