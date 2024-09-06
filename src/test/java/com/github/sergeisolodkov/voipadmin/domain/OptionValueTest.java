package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.OptionTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OptionValueTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.SettingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OptionValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionValue.class);
        OptionValue optionValue1 = getOptionValueSample1();
        OptionValue optionValue2 = new OptionValue();
        assertThat(optionValue1).isNotEqualTo(optionValue2);

        optionValue2.setId(optionValue1.getId());
        assertThat(optionValue1).isEqualTo(optionValue2);

        optionValue2 = getOptionValueSample2();
        assertThat(optionValue1).isNotEqualTo(optionValue2);
    }

    @Test
    void settingsTest() {
        OptionValue optionValue = getOptionValueRandomSampleGenerator();
        Setting settingBack = getSettingRandomSampleGenerator();

        optionValue.addSettings(settingBack);
        assertThat(optionValue.getSettings()).containsOnly(settingBack);
        assertThat(settingBack.getSelectedValues()).containsOnly(optionValue);

        optionValue.removeSettings(settingBack);
        assertThat(optionValue.getSettings()).doesNotContain(settingBack);
        assertThat(settingBack.getSelectedValues()).doesNotContain(optionValue);

        optionValue.settings(new HashSet<>(Set.of(settingBack)));
        assertThat(optionValue.getSettings()).containsOnly(settingBack);
        assertThat(settingBack.getSelectedValues()).containsOnly(optionValue);

        optionValue.setSettings(new HashSet<>());
        assertThat(optionValue.getSettings()).doesNotContain(settingBack);
        assertThat(settingBack.getSelectedValues()).doesNotContain(optionValue);
    }

    @Test
    void optionTest() {
        OptionValue optionValue = getOptionValueRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        optionValue.setOption(optionBack);
        assertThat(optionValue.getOption()).isEqualTo(optionBack);

        optionValue.option(null);
        assertThat(optionValue.getOption()).isNull();
    }
}
