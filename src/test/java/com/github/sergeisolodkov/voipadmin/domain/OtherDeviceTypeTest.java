package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.OtherDeviceTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtherDeviceTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtherDeviceType.class);
        OtherDeviceType otherDeviceType1 = getOtherDeviceTypeSample1();
        OtherDeviceType otherDeviceType2 = new OtherDeviceType();
        assertThat(otherDeviceType1).isNotEqualTo(otherDeviceType2);

        otherDeviceType2.setId(otherDeviceType1.getId());
        assertThat(otherDeviceType1).isEqualTo(otherDeviceType2);

        otherDeviceType2 = getOtherDeviceTypeSample2();
        assertThat(otherDeviceType1).isNotEqualTo(otherDeviceType2);
    }
}
