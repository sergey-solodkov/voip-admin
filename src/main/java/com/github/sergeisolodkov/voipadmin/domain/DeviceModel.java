package com.github.sergeisolodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.sergeisolodkov.voipadmin.domain.enumeration.DeviceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeviceModel.
 */
@Entity
@Table(name = "device_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "configurable", nullable = false)
    private Boolean configurable;

    @Column(name = "lines_amount")
    private Integer linesAmount;

    @Column(name = "config_template_path")
    private String configTemplatePath;

    @Column(name = "firmware_file_path")
    private String firmwareFilePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    private DeviceType deviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    private OtherDeviceType otherDeviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "options" }, allowSetters = true)
    private Vendor vendor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_device_model__options",
        joinColumns = @JoinColumn(name = "device_model_id"),
        inverseJoinColumns = @JoinColumn(name = "options_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "possibleValues", "vendors", "models" }, allowSetters = true)
    private Set<Option> options = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeviceModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DeviceModel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getConfigurable() {
        return this.configurable;
    }

    public DeviceModel configurable(Boolean configurable) {
        this.setConfigurable(configurable);
        return this;
    }

    public void setConfigurable(Boolean configurable) {
        this.configurable = configurable;
    }

    public Integer getLinesAmount() {
        return this.linesAmount;
    }

    public DeviceModel linesAmount(Integer linesAmount) {
        this.setLinesAmount(linesAmount);
        return this;
    }

    public void setLinesAmount(Integer linesAmount) {
        this.linesAmount = linesAmount;
    }

    public String getConfigTemplatePath() {
        return this.configTemplatePath;
    }

    public DeviceModel configTemplatePath(String configTemplatePath) {
        this.setConfigTemplatePath(configTemplatePath);
        return this;
    }

    public void setConfigTemplatePath(String configTemplatePath) {
        this.configTemplatePath = configTemplatePath;
    }

    public String getFirmwareFilePath() {
        return this.firmwareFilePath;
    }

    public DeviceModel firmwareFilePath(String firmwareFilePath) {
        this.setFirmwareFilePath(firmwareFilePath);
        return this;
    }

    public void setFirmwareFilePath(String firmwareFilePath) {
        this.firmwareFilePath = firmwareFilePath;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public DeviceModel deviceType(DeviceType deviceType) {
        this.setDeviceType(deviceType);
        return this;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public OtherDeviceType getOtherDeviceType() {
        return this.otherDeviceType;
    }

    public void setOtherDeviceType(OtherDeviceType otherDeviceType) {
        this.otherDeviceType = otherDeviceType;
    }

    public DeviceModel otherDeviceType(OtherDeviceType otherDeviceType) {
        this.setOtherDeviceType(otherDeviceType);
        return this;
    }

    public Vendor getVendor() {
        return this.vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public DeviceModel vendor(Vendor vendor) {
        this.setVendor(vendor);
        return this;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public DeviceModel options(Set<Option> options) {
        this.setOptions(options);
        return this;
    }

    public DeviceModel addOptions(Option option) {
        this.options.add(option);
        return this;
    }

    public DeviceModel removeOptions(Option option) {
        this.options.remove(option);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceModel)) {
            return false;
        }
        return getId() != null && getId().equals(((DeviceModel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceModel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", configurable='" + getConfigurable() + "'" +
            ", linesAmount=" + getLinesAmount() +
            ", configTemplatePath='" + getConfigTemplatePath() + "'" +
            ", firmwareFilePath='" + getFirmwareFilePath() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            "}";
    }
}
