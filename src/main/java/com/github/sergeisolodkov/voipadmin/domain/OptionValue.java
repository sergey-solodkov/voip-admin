package com.github.sergeisolodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OptionValue.
 */
@Entity
@Table(name = "option_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "selectedValues")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "option", "selectedValues", "device" }, allowSetters = true)
    private Set<Setting> settings = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "possibleValues", "vendors", "models" }, allowSetters = true)
    private Option option;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OptionValue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public OptionValue value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Setting> getSettings() {
        return this.settings;
    }

    public void setSettings(Set<Setting> settings) {
        if (this.settings != null) {
            this.settings.forEach(i -> i.removeSelectedValues(this));
        }
        if (settings != null) {
            settings.forEach(i -> i.addSelectedValues(this));
        }
        this.settings = settings;
    }

    public OptionValue settings(Set<Setting> settings) {
        this.setSettings(settings);
        return this;
    }

    public OptionValue addSettings(Setting setting) {
        this.settings.add(setting);
        setting.getSelectedValues().add(this);
        return this;
    }

    public OptionValue removeSettings(Setting setting) {
        this.settings.remove(setting);
        setting.getSelectedValues().remove(this);
        return this;
    }

    public Option getOption() {
        return this.option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public OptionValue option(Option option) {
        this.setOption(option);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionValue)) {
            return false;
        }
        return getId() != null && getId().equals(((OptionValue) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionValue{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
