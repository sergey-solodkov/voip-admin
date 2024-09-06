package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceModelTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OptionTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OtherDeviceTypeTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.VendorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeviceModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceModel.class);
        DeviceModel deviceModel1 = getDeviceModelSample1();
        DeviceModel deviceModel2 = new DeviceModel();
        assertThat(deviceModel1).isNotEqualTo(deviceModel2);

        deviceModel2.setId(deviceModel1.getId());
        assertThat(deviceModel1).isEqualTo(deviceModel2);

        deviceModel2 = getDeviceModelSample2();
        assertThat(deviceModel1).isNotEqualTo(deviceModel2);
    }

    @Test
    void otherDeviceTypeTest() {
        DeviceModel deviceModel = getDeviceModelRandomSampleGenerator();
        OtherDeviceType otherDeviceTypeBack = getOtherDeviceTypeRandomSampleGenerator();

        deviceModel.setOtherDeviceType(otherDeviceTypeBack);
        assertThat(deviceModel.getOtherDeviceType()).isEqualTo(otherDeviceTypeBack);

        deviceModel.otherDeviceType(null);
        assertThat(deviceModel.getOtherDeviceType()).isNull();
    }

    @Test
    void vendorTest() {
        DeviceModel deviceModel = getDeviceModelRandomSampleGenerator();
        Vendor vendorBack = getVendorRandomSampleGenerator();

        deviceModel.setVendor(vendorBack);
        assertThat(deviceModel.getVendor()).isEqualTo(vendorBack);

        deviceModel.vendor(null);
        assertThat(deviceModel.getVendor()).isNull();
    }

    @Test
    void optionsTest() {
        DeviceModel deviceModel = getDeviceModelRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        deviceModel.addOptions(optionBack);
        assertThat(deviceModel.getOptions()).containsOnly(optionBack);

        deviceModel.removeOptions(optionBack);
        assertThat(deviceModel.getOptions()).doesNotContain(optionBack);

        deviceModel.options(new HashSet<>(Set.of(optionBack)));
        assertThat(deviceModel.getOptions()).containsOnly(optionBack);

        deviceModel.setOptions(new HashSet<>());
        assertThat(deviceModel.getOptions()).doesNotContain(optionBack);
    }
}
