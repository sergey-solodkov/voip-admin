package com.github.sergeisolodkov.voipadmin.service.mapper;

import static com.github.sergeisolodkov.voipadmin.domain.SettingAsserts.*;
import static com.github.sergeisolodkov.voipadmin.domain.SettingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettingMapperTest {

    private SettingMapper settingMapper;

    @BeforeEach
    void setUp() {
        settingMapper = new SettingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSettingSample1();
        var actual = settingMapper.toEntity(settingMapper.toDto(expected));
        assertSettingAllPropertiesEquals(expected, actual);
    }
}
