package com.github.sergeisolodkov.voipadmin.service.criteria;

import com.github.sergeisolodkov.voipadmin.domain.enumeration.OptionValueType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.github.sergeisolodkov.voipadmin.domain.Option} entity. This class is used
 * in {@link com.github.sergeisolodkov.voipadmin.web.rest.OptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OptionValueType
     */
    public static class OptionValueTypeFilter extends Filter<OptionValueType> {

        public OptionValueTypeFilter() {}

        public OptionValueTypeFilter(OptionValueTypeFilter filter) {
            super(filter);
        }

        @Override
        public OptionValueTypeFilter copy() {
            return new OptionValueTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter descr;

    private OptionValueTypeFilter valueType;

    private BooleanFilter multiple;

    private LongFilter possibleValuesId;

    private LongFilter vendorsId;

    private LongFilter modelsId;

    private Boolean distinct;

    public OptionCriteria() {}

    public OptionCriteria(OptionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.descr = other.optionalDescr().map(StringFilter::copy).orElse(null);
        this.valueType = other.optionalValueType().map(OptionValueTypeFilter::copy).orElse(null);
        this.multiple = other.optionalMultiple().map(BooleanFilter::copy).orElse(null);
        this.possibleValuesId = other.optionalPossibleValuesId().map(LongFilter::copy).orElse(null);
        this.vendorsId = other.optionalVendorsId().map(LongFilter::copy).orElse(null);
        this.modelsId = other.optionalModelsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OptionCriteria copy() {
        return new OptionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDescr() {
        return descr;
    }

    public Optional<StringFilter> optionalDescr() {
        return Optional.ofNullable(descr);
    }

    public StringFilter descr() {
        if (descr == null) {
            setDescr(new StringFilter());
        }
        return descr;
    }

    public void setDescr(StringFilter descr) {
        this.descr = descr;
    }

    public OptionValueTypeFilter getValueType() {
        return valueType;
    }

    public Optional<OptionValueTypeFilter> optionalValueType() {
        return Optional.ofNullable(valueType);
    }

    public OptionValueTypeFilter valueType() {
        if (valueType == null) {
            setValueType(new OptionValueTypeFilter());
        }
        return valueType;
    }

    public void setValueType(OptionValueTypeFilter valueType) {
        this.valueType = valueType;
    }

    public BooleanFilter getMultiple() {
        return multiple;
    }

    public Optional<BooleanFilter> optionalMultiple() {
        return Optional.ofNullable(multiple);
    }

    public BooleanFilter multiple() {
        if (multiple == null) {
            setMultiple(new BooleanFilter());
        }
        return multiple;
    }

    public void setMultiple(BooleanFilter multiple) {
        this.multiple = multiple;
    }

    public LongFilter getPossibleValuesId() {
        return possibleValuesId;
    }

    public Optional<LongFilter> optionalPossibleValuesId() {
        return Optional.ofNullable(possibleValuesId);
    }

    public LongFilter possibleValuesId() {
        if (possibleValuesId == null) {
            setPossibleValuesId(new LongFilter());
        }
        return possibleValuesId;
    }

    public void setPossibleValuesId(LongFilter possibleValuesId) {
        this.possibleValuesId = possibleValuesId;
    }

    public LongFilter getVendorsId() {
        return vendorsId;
    }

    public Optional<LongFilter> optionalVendorsId() {
        return Optional.ofNullable(vendorsId);
    }

    public LongFilter vendorsId() {
        if (vendorsId == null) {
            setVendorsId(new LongFilter());
        }
        return vendorsId;
    }

    public void setVendorsId(LongFilter vendorsId) {
        this.vendorsId = vendorsId;
    }

    public LongFilter getModelsId() {
        return modelsId;
    }

    public Optional<LongFilter> optionalModelsId() {
        return Optional.ofNullable(modelsId);
    }

    public LongFilter modelsId() {
        if (modelsId == null) {
            setModelsId(new LongFilter());
        }
        return modelsId;
    }

    public void setModelsId(LongFilter modelsId) {
        this.modelsId = modelsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OptionCriteria that = (OptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(descr, that.descr) &&
            Objects.equals(valueType, that.valueType) &&
            Objects.equals(multiple, that.multiple) &&
            Objects.equals(possibleValuesId, that.possibleValuesId) &&
            Objects.equals(vendorsId, that.vendorsId) &&
            Objects.equals(modelsId, that.modelsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, descr, valueType, multiple, possibleValuesId, vendorsId, modelsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDescr().map(f -> "descr=" + f + ", ").orElse("") +
            optionalValueType().map(f -> "valueType=" + f + ", ").orElse("") +
            optionalMultiple().map(f -> "multiple=" + f + ", ").orElse("") +
            optionalPossibleValuesId().map(f -> "possibleValuesId=" + f + ", ").orElse("") +
            optionalVendorsId().map(f -> "vendorsId=" + f + ", ").orElse("") +
            optionalModelsId().map(f -> "modelsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
