package com.github.sergeisolodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.sergeisolodkov.voipadmin.domain.enumeration.ProvisioningMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "mac", nullable = false)
    private String mac;

    @Column(name = "inventory_id")
    private String inventoryId;

    @Column(name = "location")
    private String location;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "web_access_login")
    private String webAccessLogin;

    @Column(name = "web_access_password_hash")
    private String webAccessPasswordHash;

    @Column(name = "dhcp_enabled")
    private Boolean dhcpEnabled;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "subnet_mask")
    private String subnetMask;

    @Column(name = "default_gw")
    private String defaultGw;

    @Column(name = "dns_1")
    private String dns1;

    @Column(name = "dns_2")
    private String dns2;

    @Enumerated(EnumType.STRING)
    @Column(name = "provisioning_mode")
    private ProvisioningMode provisioningMode;

    @Column(name = "provisioning_url")
    private String provisioningUrl;

    @Column(name = "ntp")
    private String ntp;

    @Column(name = "config_path")
    private String configPath;

    @Column(name = "notes")
    private String notes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "option", "selectedValues", "device" }, allowSetters = true)
    private Set<Setting> settings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "device" }, allowSetters = true)
    private Set<VoipAccount> voipAccounts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "settings", "voipAccounts", "children", "model", "owner", "parent" }, allowSetters = true)
    private Set<Device> children = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "otherDeviceType", "vendor", "options" }, allowSetters = true)
    private DeviceModel model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "department" }, allowSetters = true)
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "settings", "voipAccounts", "children", "model", "owner", "parent" }, allowSetters = true)
    private Device parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Device id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMac() {
        return this.mac;
    }

    public Device mac(String mac) {
        this.setMac(mac);
        return this;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getInventoryId() {
        return this.inventoryId;
    }

    public Device inventoryId(String inventoryId) {
        this.setInventoryId(inventoryId);
        return this;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getLocation() {
        return this.location;
    }

    public Device location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHostname() {
        return this.hostname;
    }

    public Device hostname(String hostname) {
        this.setHostname(hostname);
        return this;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getWebAccessLogin() {
        return this.webAccessLogin;
    }

    public Device webAccessLogin(String webAccessLogin) {
        this.setWebAccessLogin(webAccessLogin);
        return this;
    }

    public void setWebAccessLogin(String webAccessLogin) {
        this.webAccessLogin = webAccessLogin;
    }

    public String getWebAccessPasswordHash() {
        return this.webAccessPasswordHash;
    }

    public Device webAccessPasswordHash(String webAccessPasswordHash) {
        this.setWebAccessPasswordHash(webAccessPasswordHash);
        return this;
    }

    public void setWebAccessPasswordHash(String webAccessPasswordHash) {
        this.webAccessPasswordHash = webAccessPasswordHash;
    }

    public Boolean getDhcpEnabled() {
        return this.dhcpEnabled;
    }

    public Device dhcpEnabled(Boolean dhcpEnabled) {
        this.setDhcpEnabled(dhcpEnabled);
        return this;
    }

    public void setDhcpEnabled(Boolean dhcpEnabled) {
        this.dhcpEnabled = dhcpEnabled;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public Device ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSubnetMask() {
        return this.subnetMask;
    }

    public Device subnetMask(String subnetMask) {
        this.setSubnetMask(subnetMask);
        return this;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getDefaultGw() {
        return this.defaultGw;
    }

    public Device defaultGw(String defaultGw) {
        this.setDefaultGw(defaultGw);
        return this;
    }

    public void setDefaultGw(String defaultGw) {
        this.defaultGw = defaultGw;
    }

    public String getDns1() {
        return this.dns1;
    }

    public Device dns1(String dns1) {
        this.setDns1(dns1);
        return this;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return this.dns2;
    }

    public Device dns2(String dns2) {
        this.setDns2(dns2);
        return this;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public ProvisioningMode getProvisioningMode() {
        return this.provisioningMode;
    }

    public Device provisioningMode(ProvisioningMode provisioningMode) {
        this.setProvisioningMode(provisioningMode);
        return this;
    }

    public void setProvisioningMode(ProvisioningMode provisioningMode) {
        this.provisioningMode = provisioningMode;
    }

    public String getProvisioningUrl() {
        return this.provisioningUrl;
    }

    public Device provisioningUrl(String provisioningUrl) {
        this.setProvisioningUrl(provisioningUrl);
        return this;
    }

    public void setProvisioningUrl(String provisioningUrl) {
        this.provisioningUrl = provisioningUrl;
    }

    public String getNtp() {
        return this.ntp;
    }

    public Device ntp(String ntp) {
        this.setNtp(ntp);
        return this;
    }

    public void setNtp(String ntp) {
        this.ntp = ntp;
    }

    public String getConfigPath() {
        return this.configPath;
    }

    public Device configPath(String configPath) {
        this.setConfigPath(configPath);
        return this;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getNotes() {
        return this.notes;
    }

    public Device notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Setting> getSettings() {
        return this.settings;
    }

    public void setSettings(Set<Setting> settings) {
        if (this.settings != null) {
            this.settings.forEach(i -> i.setDevice(null));
        }
        if (settings != null) {
            settings.forEach(i -> i.setDevice(this));
        }
        this.settings = settings;
    }

    public Device settings(Set<Setting> settings) {
        this.setSettings(settings);
        return this;
    }

    public Device addSettings(Setting setting) {
        this.settings.add(setting);
        setting.setDevice(this);
        return this;
    }

    public Device removeSettings(Setting setting) {
        this.settings.remove(setting);
        setting.setDevice(null);
        return this;
    }

    public Set<VoipAccount> getVoipAccounts() {
        return this.voipAccounts;
    }

    public void setVoipAccounts(Set<VoipAccount> voipAccounts) {
        if (this.voipAccounts != null) {
            this.voipAccounts.forEach(i -> i.setDevice(null));
        }
        if (voipAccounts != null) {
            voipAccounts.forEach(i -> i.setDevice(this));
        }
        this.voipAccounts = voipAccounts;
    }

    public Device voipAccounts(Set<VoipAccount> voipAccounts) {
        this.setVoipAccounts(voipAccounts);
        return this;
    }

    public Device addVoipAccounts(VoipAccount voipAccount) {
        this.voipAccounts.add(voipAccount);
        voipAccount.setDevice(this);
        return this;
    }

    public Device removeVoipAccounts(VoipAccount voipAccount) {
        this.voipAccounts.remove(voipAccount);
        voipAccount.setDevice(null);
        return this;
    }

    public Set<Device> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Device> devices) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (devices != null) {
            devices.forEach(i -> i.setParent(this));
        }
        this.children = devices;
    }

    public Device children(Set<Device> devices) {
        this.setChildren(devices);
        return this;
    }

    public Device addChildren(Device device) {
        this.children.add(device);
        device.setParent(this);
        return this;
    }

    public Device removeChildren(Device device) {
        this.children.remove(device);
        device.setParent(null);
        return this;
    }

    public DeviceModel getModel() {
        return this.model;
    }

    public void setModel(DeviceModel deviceModel) {
        this.model = deviceModel;
    }

    public Device model(DeviceModel deviceModel) {
        this.setModel(deviceModel);
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Device owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    public Device getParent() {
        return this.parent;
    }

    public void setParent(Device device) {
        this.parent = device;
    }

    public Device parent(Device device) {
        this.setParent(device);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return getId() != null && getId().equals(((Device) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
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
            "}";
    }
}
