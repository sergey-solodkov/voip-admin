package com.github.sergeisolodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.github.sergeisolodkov.voipadmin.domain.OptionValue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionValueDTO implements Serializable {

    private Long id;

    private String value;

    private Set<SettingDTO> settings = new HashSet<>();

    private OptionDTO option;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<SettingDTO> getSettings() {
        return settings;
    }

    public void setSettings(Set<SettingDTO> settings) {
        this.settings = settings;
    }

    public OptionDTO getOption() {
        return option;
    }

    public void setOption(OptionDTO option) {
        this.option = option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionValueDTO)) {
            return false;
        }

        OptionValueDTO optionValueDTO = (OptionValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, optionValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionValueDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", settings=" + getSettings() +
            ", option=" + getOption() +
            "}";
    }
}
