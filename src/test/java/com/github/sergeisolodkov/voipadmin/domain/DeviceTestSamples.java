package com.github.sergeisolodkov.voipadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Device getDeviceSample1() {
        return new Device()
            .id(1L)
            .mac("mac1")
            .inventoryId("inventoryId1")
            .location("location1")
            .hostname("hostname1")
            .webAccessLogin("webAccessLogin1")
            .webAccessPasswordHash("webAccessPasswordHash1")
            .ipAddress("ipAddress1")
            .subnetMask("subnetMask1")
            .defaultGw("defaultGw1")
            .dns1("dns11")
            .dns2("dns21")
            .provisioningUrl("provisioningUrl1")
            .ntp("ntp1")
            .configPath("configPath1")
            .notes("notes1");
    }

    public static Device getDeviceSample2() {
        return new Device()
            .id(2L)
            .mac("mac2")
            .inventoryId("inventoryId2")
            .location("location2")
            .hostname("hostname2")
            .webAccessLogin("webAccessLogin2")
            .webAccessPasswordHash("webAccessPasswordHash2")
            .ipAddress("ipAddress2")
            .subnetMask("subnetMask2")
            .defaultGw("defaultGw2")
            .dns1("dns12")
            .dns2("dns22")
            .provisioningUrl("provisioningUrl2")
            .ntp("ntp2")
            .configPath("configPath2")
            .notes("notes2");
    }

    public static Device getDeviceRandomSampleGenerator() {
        return new Device()
            .id(longCount.incrementAndGet())
            .mac(UUID.randomUUID().toString())
            .inventoryId(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .hostname(UUID.randomUUID().toString())
            .webAccessLogin(UUID.randomUUID().toString())
            .webAccessPasswordHash(UUID.randomUUID().toString())
            .ipAddress(UUID.randomUUID().toString())
            .subnetMask(UUID.randomUUID().toString())
            .defaultGw(UUID.randomUUID().toString())
            .dns1(UUID.randomUUID().toString())
            .dns2(UUID.randomUUID().toString())
            .provisioningUrl(UUID.randomUUID().toString())
            .ntp(UUID.randomUUID().toString())
            .configPath(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
