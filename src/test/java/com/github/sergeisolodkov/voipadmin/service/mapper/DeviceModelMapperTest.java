package com.github.sergeisolodkov.voipadmin.service.mapper;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceModelAsserts.*;
import static com.github.sergeisolodkov.voipadmin.domain.DeviceModelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceModelMapperTest {

    private DeviceModelMapper deviceModelMapper;

    @BeforeEach
    void setUp() {
        deviceModelMapper = new DeviceModelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDeviceModelSample1();
        var actual = deviceModelMapper.toEntity(deviceModelMapper.toDto(expected));
        assertDeviceModelAllPropertiesEquals(expected, actual);
    }
}
