package com.github.sergeisolodkov.voipadmin.service.mapper;

import static com.github.sergeisolodkov.voipadmin.domain.OptionValueAsserts.*;
import static com.github.sergeisolodkov.voipadmin.domain.OptionValueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OptionValueMapperTest {

    private OptionValueMapper optionValueMapper;

    @BeforeEach
    void setUp() {
        optionValueMapper = new OptionValueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOptionValueSample1();
        var actual = optionValueMapper.toEntity(optionValueMapper.toDto(expected));
        assertOptionValueAllPropertiesEquals(expected, actual);
    }
}
