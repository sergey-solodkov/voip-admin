package com.github.sergeisolodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.github.sergeisolodkov.voipadmin.domain.VoipAccount} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoipAccountDTO implements Serializable {

    private Long id;

    private Boolean manual;

    private String username;

    private String passwordHash;

    private String sipServer;

    private String sipPort;

    private Boolean lineEnable;

    private Integer lineNumber;

    private DeviceDTO device;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSipServer() {
        return sipServer;
    }

    public void setSipServer(String sipServer) {
        this.sipServer = sipServer;
    }

    public String getSipPort() {
        return sipPort;
    }

    public void setSipPort(String sipPort) {
        this.sipPort = sipPort;
    }

    public Boolean getLineEnable() {
        return lineEnable;
    }

    public void setLineEnable(Boolean lineEnable) {
        this.lineEnable = lineEnable;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoipAccountDTO)) {
            return false;
        }

        VoipAccountDTO voipAccountDTO = (VoipAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, voipAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoipAccountDTO{" +
            "id=" + getId() +
            ", manual='" + getManual() + "'" +
            ", username='" + getUsername() + "'" +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", sipServer='" + getSipServer() + "'" +
            ", sipPort='" + getSipPort() + "'" +
            ", lineEnable='" + getLineEnable() + "'" +
            ", lineNumber=" + getLineNumber() +
            ", device=" + getDevice() +
            "}";
    }
}
