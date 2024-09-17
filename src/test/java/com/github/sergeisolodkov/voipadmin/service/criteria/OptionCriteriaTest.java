package com.github.sergeisolodkov.voipadmin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OptionCriteriaTest {

    @Test
    void newOptionCriteriaHasAllFiltersNullTest() {
        var optionCriteria = new OptionCriteria();
        assertThat(optionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void optionCriteriaFluentMethodsCreatesFiltersTest() {
        var optionCriteria = new OptionCriteria();

        setAllFilters(optionCriteria);

        assertThat(optionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void optionCriteriaCopyCreatesNullFilterTest() {
        var optionCriteria = new OptionCriteria();
        var copy = optionCriteria.copy();

        assertThat(optionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(optionCriteria)
        );
    }

    @Test
    void optionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var optionCriteria = new OptionCriteria();
        setAllFilters(optionCriteria);

        var copy = optionCriteria.copy();

        assertThat(optionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(optionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var optionCriteria = new OptionCriteria();

        assertThat(optionCriteria).hasToString("OptionCriteria{}");
    }

    private static void setAllFilters(OptionCriteria optionCriteria) {
        optionCriteria.id();
        optionCriteria.code();
        optionCriteria.descr();
        optionCriteria.valueType();
        optionCriteria.multiple();
        optionCriteria.possibleValuesId();
        optionCriteria.vendorsId();
        optionCriteria.modelsId();
        optionCriteria.distinct();
    }

    private static Condition<OptionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDescr()) &&
                condition.apply(criteria.getValueType()) &&
                condition.apply(criteria.getMultiple()) &&
                condition.apply(criteria.getPossibleValuesId()) &&
                condition.apply(criteria.getVendorsId()) &&
                condition.apply(criteria.getModelsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OptionCriteria> copyFiltersAre(OptionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDescr(), copy.getDescr()) &&
                condition.apply(criteria.getValueType(), copy.getValueType()) &&
                condition.apply(criteria.getMultiple(), copy.getMultiple()) &&
                condition.apply(criteria.getPossibleValuesId(), copy.getPossibleValuesId()) &&
                condition.apply(criteria.getVendorsId(), copy.getVendorsId()) &&
                condition.apply(criteria.getModelsId(), copy.getModelsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
