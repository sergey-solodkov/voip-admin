package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OptionTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OptionValueTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.SettingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SettingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Setting.class);
        Setting setting1 = getSettingSample1();
        Setting setting2 = new Setting();
        assertThat(setting1).isNotEqualTo(setting2);

        setting2.setId(setting1.getId());
        assertThat(setting1).isEqualTo(setting2);

        setting2 = getSettingSample2();
        assertThat(setting1).isNotEqualTo(setting2);
    }

    @Test
    void optionTest() {
        Setting setting = getSettingRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        setting.setOption(optionBack);
        assertThat(setting.getOption()).isEqualTo(optionBack);

        setting.option(null);
        assertThat(setting.getOption()).isNull();
    }

    @Test
    void selectedValuesTest() {
        Setting setting = getSettingRandomSampleGenerator();
        OptionValue optionValueBack = getOptionValueRandomSampleGenerator();

        setting.addSelectedValues(optionValueBack);
        assertThat(setting.getSelectedValues()).containsOnly(optionValueBack);

        setting.removeSelectedValues(optionValueBack);
        assertThat(setting.getSelectedValues()).doesNotContain(optionValueBack);

        setting.selectedValues(new HashSet<>(Set.of(optionValueBack)));
        assertThat(setting.getSelectedValues()).containsOnly(optionValueBack);

        setting.setSelectedValues(new HashSet<>());
        assertThat(setting.getSelectedValues()).doesNotContain(optionValueBack);
    }

    @Test
    void deviceTest() {
        Setting setting = getSettingRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        setting.setDevice(deviceBack);
        assertThat(setting.getDevice()).isEqualTo(deviceBack);

        setting.device(null);
        assertThat(setting.getDevice()).isNull();
    }
}
