package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.VoipAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoipAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoipAccount.class);
        VoipAccount voipAccount1 = getVoipAccountSample1();
        VoipAccount voipAccount2 = new VoipAccount();
        assertThat(voipAccount1).isNotEqualTo(voipAccount2);

        voipAccount2.setId(voipAccount1.getId());
        assertThat(voipAccount1).isEqualTo(voipAccount2);

        voipAccount2 = getVoipAccountSample2();
        assertThat(voipAccount1).isNotEqualTo(voipAccount2);
    }

    @Test
    void deviceTest() {
        VoipAccount voipAccount = getVoipAccountRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        voipAccount.setDevice(deviceBack);
        assertThat(voipAccount.getDevice()).isEqualTo(deviceBack);

        voipAccount.device(null);
        assertThat(voipAccount.getDevice()).isNull();
    }
}
