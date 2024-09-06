package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.DepartmentTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OwnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Owner.class);
        Owner owner1 = getOwnerSample1();
        Owner owner2 = new Owner();
        assertThat(owner1).isNotEqualTo(owner2);

        owner2.setId(owner1.getId());
        assertThat(owner1).isEqualTo(owner2);

        owner2 = getOwnerSample2();
        assertThat(owner1).isNotEqualTo(owner2);
    }

    @Test
    void departmentTest() {
        Owner owner = getOwnerRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        owner.setDepartment(departmentBack);
        assertThat(owner.getDepartment()).isEqualTo(departmentBack);

        owner.department(null);
        assertThat(owner.getDepartment()).isNull();
    }
}
