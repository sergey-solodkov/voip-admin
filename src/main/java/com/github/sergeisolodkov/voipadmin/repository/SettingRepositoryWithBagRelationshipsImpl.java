package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.Setting;
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
public class SettingRepositoryWithBagRelationshipsImpl implements SettingRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SETTINGS_PARAMETER = "settings";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Setting> fetchBagRelationships(Optional<Setting> setting) {
        return setting.map(this::fetchSelectedValues);
    }

    @Override
    public Page<Setting> fetchBagRelationships(Page<Setting> settings) {
        return new PageImpl<>(fetchBagRelationships(settings.getContent()), settings.getPageable(), settings.getTotalElements());
    }

    @Override
    public List<Setting> fetchBagRelationships(List<Setting> settings) {
        return Optional.of(settings).map(this::fetchSelectedValues).orElse(Collections.emptyList());
    }

    Setting fetchSelectedValues(Setting result) {
        return entityManager
            .createQuery("select setting from Setting setting left join fetch setting.selectedValues where setting.id = :id", Setting.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Setting> fetchSelectedValues(List<Setting> settings) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, settings.size()).forEach(index -> order.put(settings.get(index).getId(), index));
        List<Setting> result = entityManager
            .createQuery(
                "select setting from Setting setting left join fetch setting.selectedValues where setting in :settings",
                Setting.class
            )
            .setParameter(SETTINGS_PARAMETER, settings)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
