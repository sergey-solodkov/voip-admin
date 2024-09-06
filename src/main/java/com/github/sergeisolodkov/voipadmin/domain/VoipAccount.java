package com.github.sergeisolodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VoipAccount.
 */
@Entity
@Table(name = "voip_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoipAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "manual")
    private Boolean manual;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "sip_server")
    private String sipServer;

    @Column(name = "sip_port")
    private String sipPort;

    @Column(name = "line_enable")
    private Boolean lineEnable;

    @Column(name = "line_number")
    private Integer lineNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "settings", "voipAccounts", "children", "model", "owner", "parent" }, allowSetters = true)
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VoipAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getManual() {
        return this.manual;
    }

    public VoipAccount manual(Boolean manual) {
        this.setManual(manual);
        return this;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public String getUsername() {
        return this.username;
    }

    public VoipAccount username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public VoipAccount passwordHash(String passwordHash) {
        this.setPasswordHash(passwordHash);
        return this;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSipServer() {
        return this.sipServer;
    }

    public VoipAccount sipServer(String sipServer) {
        this.setSipServer(sipServer);
        return this;
    }

    public void setSipServer(String sipServer) {
        this.sipServer = sipServer;
    }

    public String getSipPort() {
        return this.sipPort;
    }

    public VoipAccount sipPort(String sipPort) {
        this.setSipPort(sipPort);
        return this;
    }

    public void setSipPort(String sipPort) {
        this.sipPort = sipPort;
    }

    public Boolean getLineEnable() {
        return this.lineEnable;
    }

    public VoipAccount lineEnable(Boolean lineEnable) {
        this.setLineEnable(lineEnable);
        return this;
    }

    public void setLineEnable(Boolean lineEnable) {
        this.lineEnable = lineEnable;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }

    public VoipAccount lineNumber(Integer lineNumber) {
        this.setLineNumber(lineNumber);
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public VoipAccount device(Device device) {
        this.setDevice(device);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoipAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((VoipAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoipAccount{" +
            "id=" + getId() +
            ", manual='" + getManual() + "'" +
            ", username='" + getUsername() + "'" +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", sipServer='" + getSipServer() + "'" +
            ", sipPort='" + getSipPort() + "'" +
            ", lineEnable='" + getLineEnable() + "'" +
            ", lineNumber=" + getLineNumber() +
            "}";
    }
}
