package com.github.sergeisolodkov.voipadmin.service.dto;

import com.github.sergeisolodkov.voipadmin.domain.enumeration.ProvisioningMode;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.github.sergeisolodkov.voipadmin.domain.Device} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private String mac;

    private String inventoryId;

    private String location;

    private String hostname;

    private String webAccessLogin;

    private String webAccessPasswordHash;

    private Boolean dhcpEnabled;

    private String ipAddress;

    private String subnetMask;

    private String defaultGw;

    private String dns1;

    private String dns2;

    private ProvisioningMode provisioningMode;

    private String provisioningUrl;

    private String ntp;

    private String configPath;

    private String notes;

    private DeviceModelDTO model;

    private OwnerDTO owner;

    private DeviceDTO parent;

    private Set<VoipAccountDTO> voipAccounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getWebAccessLogin() {
        return webAccessLogin;
    }

    public void setWebAccessLogin(String webAccessLogin) {
        this.webAccessLogin = webAccessLogin;
    }

    public String getWebAccessPasswordHash() {
        return webAccessPasswordHash;
    }

    public void setWebAccessPasswordHash(String webAccessPasswordHash) {
        this.webAccessPasswordHash = webAccessPasswordHash;
    }

    public Boolean getDhcpEnabled() {
        return dhcpEnabled;
    }

    public void setDhcpEnabled(Boolean dhcpEnabled) {
        this.dhcpEnabled = dhcpEnabled;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getDefaultGw() {
        return defaultGw;
    }

    public void setDefaultGw(String defaultGw) {
        this.defaultGw = defaultGw;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public ProvisioningMode getProvisioningMode() {
        return provisioningMode;
    }

    public void setProvisioningMode(ProvisioningMode provisioningMode) {
        this.provisioningMode = provisioningMode;
    }

    public String getProvisioningUrl() {
        return provisioningUrl;
    }

    public void setProvisioningUrl(String provisioningUrl) {
        this.provisioningUrl = provisioningUrl;
    }

    public String getNtp() {
        return ntp;
    }

    public void setNtp(String ntp) {
        this.ntp = ntp;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DeviceModelDTO getModel() {
        return model;
    }

    public void setModel(DeviceModelDTO model) {
        this.model = model;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    public DeviceDTO getParent() {
        return parent;
    }

    public void setParent(DeviceDTO parent) {
        this.parent = parent;
    }

    public Set<VoipAccountDTO> getVoipAccounts() {
        return voipAccounts;
    }

    public void setVoipAccounts(Set<VoipAccountDTO> voipAccounts) {
        this.voipAccounts = voipAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceDTO)) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id=" + getId() +
            ", mac='" + getMac() + "'" +
            ", inventoryId='" + getInventoryId() + "'" +
            ", location='" + getLocation() + "'" +
            ", hostname='" + getHostname() + "'" +
            ", webAccessLogin='" + getWebAccessLogin() + "'" +
            ", webAccessPasswordHash='" + getWebAccessPasswordHash() + "'" +
            ", dhcpEnabled='" + getDhcpEnabled() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", subnetMask='" + getSubnetMask() + "'" +
            ", defaultGw='" + getDefaultGw() + "'" +
            ", dns1='" + getDns1() + "'" +
            ", dns2='" + getDns2() + "'" +
            ", provisioningMode='" + getProvisioningMode() + "'" +
            ", provisioningUrl='" + getProvisioningUrl() + "'" +
            ", ntp='" + getNtp() + "'" +
            ", configPath='" + getConfigPath() + "'" +
            ", notes='" + getNotes() + "'" +
            ", model=" + getModel() +
            ", owner=" + getOwner() +
            ", parent=" + getParent() +
            ", voipAccounts=" + getVoipAccounts() +
            "}";
    }
}
