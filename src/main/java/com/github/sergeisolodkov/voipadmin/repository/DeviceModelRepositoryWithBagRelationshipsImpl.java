package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
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
public class DeviceModelRepositoryWithBagRelationshipsImpl implements DeviceModelRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String DEVICEMODELS_PARAMETER = "deviceModels";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<DeviceModel> fetchBagRelationships(Optional<DeviceModel> deviceModel) {
        return deviceModel.map(this::fetchOptions);
    }

    @Override
    public Page<DeviceModel> fetchBagRelationships(Page<DeviceModel> deviceModels) {
        return new PageImpl<>(
            fetchBagRelationships(deviceModels.getContent()),
            deviceModels.getPageable(),
            deviceModels.getTotalElements()
        );
    }

    @Override
    public List<DeviceModel> fetchBagRelationships(List<DeviceModel> deviceModels) {
        return Optional.of(deviceModels).map(this::fetchOptions).orElse(Collections.emptyList());
    }

    DeviceModel fetchOptions(DeviceModel result) {
        return entityManager
            .createQuery(
                "select deviceModel from DeviceModel deviceModel left join fetch deviceModel.options where deviceModel.id = :id",
                DeviceModel.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<DeviceModel> fetchOptions(List<DeviceModel> deviceModels) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, deviceModels.size()).forEach(index -> order.put(deviceModels.get(index).getId(), index));
        List<DeviceModel> result = entityManager
            .createQuery(
                "select deviceModel from DeviceModel deviceModel left join fetch deviceModel.options where deviceModel in :deviceModels",
                DeviceModel.class
            )
            .setParameter(DEVICEMODELS_PARAMETER, deviceModels)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
