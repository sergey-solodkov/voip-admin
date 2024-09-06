package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.Setting;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SettingRepositoryWithBagRelationships {
    Optional<Setting> fetchBagRelationships(Optional<Setting> setting);

    List<Setting> fetchBagRelationships(List<Setting> settings);

    Page<Setting> fetchBagRelationships(Page<Setting> settings);
}
