package com.github.sergeisolodkov.voipadmin.service.mapper;

import static com.github.sergeisolodkov.voipadmin.domain.OtherDeviceTypeAsserts.*;
import static com.github.sergeisolodkov.voipadmin.domain.OtherDeviceTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OtherDeviceTypeMapperTest {

    private OtherDeviceTypeMapper otherDeviceTypeMapper;

    @BeforeEach
    void setUp() {
        otherDeviceTypeMapper = new OtherDeviceTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOtherDeviceTypeSample1();
        var actual = otherDeviceTypeMapper.toEntity(otherDeviceTypeMapper.toDto(expected));
        assertOtherDeviceTypeAllPropertiesEquals(expected, actual);
    }
}
