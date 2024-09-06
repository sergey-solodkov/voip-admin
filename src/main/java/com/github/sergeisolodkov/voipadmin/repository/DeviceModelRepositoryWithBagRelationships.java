package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DeviceModelRepositoryWithBagRelationships {
    Optional<DeviceModel> fetchBagRelationships(Optional<DeviceModel> deviceModel);

    List<DeviceModel> fetchBagRelationships(List<DeviceModel> deviceModels);

    Page<DeviceModel> fetchBagRelationships(Page<DeviceModel> deviceModels);
}
