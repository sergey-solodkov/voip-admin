package com.github.sergeisolodkov.voipadmin.service.dto;

import com.github.sergeisolodkov.voipadmin.domain.enumeration.OptionValueType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.github.sergeisolodkov.voipadmin.domain.Option} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionDTO implements Serializable {

    private Long id;

    private String code;

    private String descr;

    private OptionValueType valueType;

    private Boolean multiple;

    private Set<VendorDTO> vendors = new HashSet<>();

    private Set<DeviceModelDTO> models = new HashSet<>();

    private Set<OptionValueDTO> possibleValues = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public OptionValueType getValueType() {
        return valueType;
    }

    public void setValueType(OptionValueType valueType) {
        this.valueType = valueType;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Set<VendorDTO> getVendors() {
        return vendors;
    }

    public void setVendors(Set<VendorDTO> vendors) {
        this.vendors = vendors;
    }

    public Set<DeviceModelDTO> getModels() {
        return models;
    }

    public void setModels(Set<DeviceModelDTO> models) {
        this.models = models;
    }

    public Set<OptionValueDTO> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(Set<OptionValueDTO> possibleValues) {
        this.possibleValues = possibleValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionDTO)) {
            return false;
        }

        OptionDTO optionDTO = (OptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, optionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", descr='" + getDescr() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", multiple='" + getMultiple() + "'" +
            ", vendors=" + getVendors() +
            ", models=" + getModels() +
            "}";
    }
}
