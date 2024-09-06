package com.github.sergeisolodkov.voipadmin.service.mapper;

import static com.github.sergeisolodkov.voipadmin.domain.OptionAsserts.*;
import static com.github.sergeisolodkov.voipadmin.domain.OptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OptionMapperTest {

    private OptionMapper optionMapper;

    @BeforeEach
    void setUp() {
        optionMapper = new OptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOptionSample1();
        var actual = optionMapper.toEntity(optionMapper.toDto(expected));
        assertOptionAllPropertiesEquals(expected, actual);
    }
}
