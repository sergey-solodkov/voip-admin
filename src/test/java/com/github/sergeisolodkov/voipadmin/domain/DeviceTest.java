package com.github.sergeisolodkov.voipadmin.domain;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceModelTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.DeviceTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.DeviceTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.OwnerTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.SettingTestSamples.*;
import static com.github.sergeisolodkov.voipadmin.domain.VoipAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.sergeisolodkov.voipadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Device.class);
        Device device1 = getDeviceSample1();
        Device device2 = new Device();
        assertThat(device1).isNotEqualTo(device2);

        device2.setId(device1.getId());
        assertThat(device1).isEqualTo(device2);

        device2 = getDeviceSample2();
        assertThat(device1).isNotEqualTo(device2);
    }

    @Test
    void settingsTest() {
        Device device = getDeviceRandomSampleGenerator();
        Setting settingBack = getSettingRandomSampleGenerator();

        device.addSettings(settingBack);
        assertThat(device.getSettings()).containsOnly(settingBack);
        assertThat(settingBack.getDevice()).isEqualTo(device);

        device.removeSettings(settingBack);
        assertThat(device.getSettings()).doesNotContain(settingBack);
        assertThat(settingBack.getDevice()).isNull();

        device.settings(new HashSet<>(Set.of(settingBack)));
        assertThat(device.getSettings()).containsOnly(settingBack);
        assertThat(settingBack.getDevice()).isEqualTo(device);

        device.setSettings(new HashSet<>());
        assertThat(device.getSettings()).doesNotContain(settingBack);
        assertThat(settingBack.getDevice()).isNull();
    }

    @Test
    void voipAccountsTest() {
        Device device = getDeviceRandomSampleGenerator();
        VoipAccount voipAccountBack = getVoipAccountRandomSampleGenerator();

        device.addVoipAccounts(voipAccountBack);
        assertThat(device.getVoipAccounts()).containsOnly(voipAccountBack);
        assertThat(voipAccountBack.getDevice()).isEqualTo(device);

        device.removeVoipAccounts(voipAccountBack);
        assertThat(device.getVoipAccounts()).doesNotContain(voipAccountBack);
        assertThat(voipAccountBack.getDevice()).isNull();

        device.voipAccounts(new HashSet<>(Set.of(voipAccountBack)));
        assertThat(device.getVoipAccounts()).containsOnly(voipAccountBack);
        assertThat(voipAccountBack.getDevice()).isEqualTo(device);

        device.setVoipAccounts(new HashSet<>());
        assertThat(device.getVoipAccounts()).doesNotContain(voipAccountBack);
        assertThat(voipAccountBack.getDevice()).isNull();
    }

    @Test
    void childrenTest() {
        Device device = getDeviceRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        device.addChildren(deviceBack);
        assertThat(device.getChildren()).containsOnly(deviceBack);
        assertThat(deviceBack.getParent()).isEqualTo(device);

        device.removeChildren(deviceBack);
        assertThat(device.getChildren()).doesNotContain(deviceBack);
        assertThat(deviceBack.getParent()).isNull();

        device.children(new HashSet<>(Set.of(deviceBack)));
        assertThat(device.getChildren()).containsOnly(deviceBack);
        assertThat(deviceBack.getParent()).isEqualTo(device);

        device.setChildren(new HashSet<>());
        assertThat(device.getChildren()).doesNotContain(deviceBack);
        assertThat(deviceBack.getParent()).isNull();
    }

    @Test
    void modelTest() {
        Device device = getDeviceRandomSampleGenerator();
        DeviceModel deviceModelBack = getDeviceModelRandomSampleGenerator();

        device.setModel(deviceModelBack);
        assertThat(device.getModel()).isEqualTo(deviceModelBack);

        device.model(null);
        assertThat(device.getModel()).isNull();
    }

    @Test
    void ownerTest() {
        Device device = getDeviceRandomSampleGenerator();
        Owner ownerBack = getOwnerRandomSampleGenerator();

        device.setOwner(ownerBack);
        assertThat(device.getOwner()).isEqualTo(ownerBack);

        device.owner(null);
        assertThat(device.getOwner()).isNull();
    }

    @Test
    void parentTest() {
        Device device = getDeviceRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        device.setParent(deviceBack);
        assertThat(device.getParent()).isEqualTo(deviceBack);

        device.parent(null);
        assertThat(device.getParent()).isNull();
    }
}
