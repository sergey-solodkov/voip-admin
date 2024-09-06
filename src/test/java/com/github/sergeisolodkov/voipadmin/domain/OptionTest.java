package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceModelTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OptionTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OptionValueTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.VendorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Option.class);
        Option option1 = getOptionSample1();
        Option option2 = new Option();
        assertThat(option1).isNotEqualTo(option2);

        option2.setId(option1.getId());
        assertThat(option1).isEqualTo(option2);

        option2 = getOptionSample2();
        assertThat(option1).isNotEqualTo(option2);
    }

    @Test
    void possibleValuesTest() {
        Option option = getOptionRandomSampleGenerator();
        OptionValue optionValueBack = getOptionValueRandomSampleGenerator();

        option.addPossibleValues(optionValueBack);
        assertThat(option.getPossibleValues()).containsOnly(optionValueBack);
        assertThat(optionValueBack.getOption()).isEqualTo(option);

        option.removePossibleValues(optionValueBack);
        assertThat(option.getPossibleValues()).doesNotContain(optionValueBack);
        assertThat(optionValueBack.getOption()).isNull();

        option.possibleValues(new HashSet<>(Set.of(optionValueBack)));
        assertThat(option.getPossibleValues()).containsOnly(optionValueBack);
        assertThat(optionValueBack.getOption()).isEqualTo(option);

        option.setPossibleValues(new HashSet<>());
        assertThat(option.getPossibleValues()).doesNotContain(optionValueBack);
        assertThat(optionValueBack.getOption()).isNull();
    }

    @Test
    void vendorsTest() {
        Option option = getOptionRandomSampleGenerator();
        Vendor vendorBack = getVendorRandomSampleGenerator();

        option.addVendors(vendorBack);
        assertThat(option.getVendors()).containsOnly(vendorBack);

        option.removeVendors(vendorBack);
        assertThat(option.getVendors()).doesNotContain(vendorBack);

        option.vendors(new HashSet<>(Set.of(vendorBack)));
        assertThat(option.getVendors()).containsOnly(vendorBack);

        option.setVendors(new HashSet<>());
        assertThat(option.getVendors()).doesNotContain(vendorBack);
    }

    @Test
    void modelsTest() {
        Option option = getOptionRandomSampleGenerator();
        DeviceModel deviceModelBack = getDeviceModelRandomSampleGenerator();

        option.addModels(deviceModelBack);
        assertThat(option.getModels()).containsOnly(deviceModelBack);
        assertThat(deviceModelBack.getOptions()).containsOnly(option);

        option.removeModels(deviceModelBack);
        assertThat(option.getModels()).doesNotContain(deviceModelBack);
        assertThat(deviceModelBack.getOptions()).doesNotContain(option);

        option.models(new HashSet<>(Set.of(deviceModelBack)));
        assertThat(option.getModels()).containsOnly(deviceModelBack);
        assertThat(deviceModelBack.getOptions()).containsOnly(option);

        option.setModels(new HashSet<>());
        assertThat(option.getModels()).doesNotContain(deviceModelBack);
        assertThat(deviceModelBack.getOptions()).doesNotContain(option);
    }
}
