package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.OptionTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.VendorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VendorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendor.class);
        Vendor vendor1 = getVendorSample1();
        Vendor vendor2 = new Vendor();
        assertThat(vendor1).isNotEqualTo(vendor2);

        vendor2.setId(vendor1.getId());
        assertThat(vendor1).isEqualTo(vendor2);

        vendor2 = getVendorSample2();
        assertThat(vendor1).isNotEqualTo(vendor2);
    }

    @Test
    void optionsTest() {
        Vendor vendor = getVendorRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        vendor.addOptions(optionBack);
        assertThat(vendor.getOptions()).containsOnly(optionBack);
        assertThat(optionBack.getVendors()).containsOnly(vendor);

        vendor.removeOptions(optionBack);
        assertThat(vendor.getOptions()).doesNotContain(optionBack);
        assertThat(optionBack.getVendors()).doesNotContain(vendor);

        vendor.options(new HashSet<>(Set.of(optionBack)));
        assertThat(vendor.getOptions()).containsOnly(optionBack);
        assertThat(optionBack.getVendors()).containsOnly(vendor);

        vendor.setOptions(new HashSet<>());
        assertThat(vendor.getOptions()).doesNotContain(optionBack);
        assertThat(optionBack.getVendors()).doesNotContain(vendor);
    }
}
