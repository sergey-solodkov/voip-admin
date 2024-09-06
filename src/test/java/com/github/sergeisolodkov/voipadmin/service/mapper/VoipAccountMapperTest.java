package com.github.sergeisolodkov.voipadmin.service.mapper;

import static com.github.sergeisolodkov.voipadmin.domain.VoipAccountAsserts.*;
import static com.github.sergeisolodkov.voipadmin.domain.VoipAccountTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VoipAccountMapperTest {

    private VoipAccountMapper voipAccountMapper;

    @BeforeEach
    void setUp() {
        voipAccountMapper = new VoipAccountMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVoipAccountSample1();
        var actual = voipAccountMapper.toEntity(voipAccountMapper.toDto(expected));
        assertVoipAccountAllPropertiesEquals(expected, actual);
    }
}
