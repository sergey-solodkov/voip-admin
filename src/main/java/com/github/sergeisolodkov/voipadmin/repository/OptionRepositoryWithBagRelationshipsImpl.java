package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.Option;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OptionRepositoryWithBagRelationshipsImpl implements OptionRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String OPTIONS_PARAMETER = "options";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Option> fetchBagRelationships(Optional<Option> option) {
        return option.map(this::fetchVendors);
    }

    @Override
    public Page<Option> fetchBagRelationships(Page<Option> options) {
        return new PageImpl<>(fetchBagRelationships(options.getContent()), options.getPageable(), options.getTotalElements());
    }

    @Override
    public List<Option> fetchBagRelationships(List<Option> options) {
        return Optional.of(options).map(this::fetchVendors).orElse(Collections.emptyList());
    }

    Option fetchVendors(Option result) {
        return entityManager
            .createQuery("select option from Option option left join fetch option.vendors where option.id = :id", Option.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Option> fetchVendors(List<Option> options) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, options.size()).forEach(index -> order.put(options.get(index).getId(), index));
        List<Option> result = entityManager
            .createQuery("select option from Option option left join fetch option.vendors where option in :options", Option.class)
            .setParameter(OPTIONS_PARAMETER, options)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
