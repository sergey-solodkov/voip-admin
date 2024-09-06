package com.github.sergeisolodkov.voipadmin.service.dto;

import com.github.sergeisolodkov.voipadmin.domain.enumeration.DeviceType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.github.sergeisolodkov.voipadmin.domain.DeviceModel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceModelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean configurable;

    private Integer linesAmount;

    private String configTemplatePath;

    private String firmwareFilePath;

    private DeviceType deviceType;

    private OtherDeviceTypeDTO otherDeviceType;

    private VendorDTO vendor;

    private Set<OptionDTO> options = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getConfigurable() {
        return configurable;
    }

    public void setConfigurable(Boolean configurable) {
        this.configurable = configurable;
    }

    public Integer getLinesAmount() {
        return linesAmount;
    }

    public void setLinesAmount(Integer linesAmount) {
        this.linesAmount = linesAmount;
    }

    public String getConfigTemplatePath() {
        return configTemplatePath;
    }

    public void setConfigTemplatePath(String configTemplatePath) {
        this.configTemplatePath = configTemplatePath;
    }

    public String getFirmwareFilePath() {
        return firmwareFilePath;
    }

    public void setFirmwareFilePath(String firmwareFilePath) {
        this.firmwareFilePath = firmwareFilePath;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public OtherDeviceTypeDTO getOtherDeviceType() {
        return otherDeviceType;
    }

    public void setOtherDeviceType(OtherDeviceTypeDTO otherDeviceType) {
        this.otherDeviceType = otherDeviceType;
    }

    public VendorDTO getVendor() {
        return vendor;
    }

    public void setVendor(VendorDTO vendor) {
        this.vendor = vendor;
    }

    public Set<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(Set<OptionDTO> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceModelDTO)) {
            return false;
        }

        DeviceModelDTO deviceModelDTO = (DeviceModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceModelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", configurable='" + getConfigurable() + "'" +
            ", linesAmount=" + getLinesAmount() +
            ", configTemplatePath='" + getConfigTemplatePath() + "'" +
            ", firmwareFilePath='" + getFirmwareFilePath() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", otherDeviceType=" + getOtherDeviceType() +
            ", vendor=" + getVendor() +
            ", options=" + getOptions() +
            "}";
    }
}
