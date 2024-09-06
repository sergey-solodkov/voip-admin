package com.github.sergeisolodkov.voipadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OptionValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionValueDTO.class);
        OptionValueDTO optionValueDTO1 = new OptionValueDTO();
        optionValueDTO1.setId(1L);
        OptionValueDTO optionValueDTO2 = new OptionValueDTO();
        assertThat(optionValueDTO1).isNotEqualTo(optionValueDTO2);
        optionValueDTO2.setId(optionValueDTO1.getId());
        assertThat(optionValueDTO1).isEqualTo(optionValueDTO2);
        optionValueDTO2.setId(2L);
        assertThat(optionValueDTO1).isNotEqualTo(optionValueDTO2);
        optionValueDTO1.setId(null);
        assertThat(optionValueDTO1).isNotEqualTo(optionValueDTO2);
    }
}
