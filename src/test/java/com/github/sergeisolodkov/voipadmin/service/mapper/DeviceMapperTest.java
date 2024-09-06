package com.github.sergeisolodkov.voipadmin.service.mapper;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceAsserts.*;
import static com.github.sergeisolodkov.voipadmin.domain.DeviceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceMapperTest {

    private DeviceMapper deviceMapper;

    @BeforeEach
    void setUp() {
        deviceMapper = new DeviceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDeviceSample1();
        var actual = deviceMapper.toEntity(deviceMapper.toDto(expected));
        assertDeviceAllPropertiesEquals(expected, actual);
    }
}
