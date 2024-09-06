package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OptionRepositoryWithBagRelationships {
    Optional<Option> fetchBagRelationships(Optional<Option> option);

    List<Option> fetchBagRelationships(List<Option> options);

    Page<Option> fetchBagRelationships(Page<Option> options);
}
