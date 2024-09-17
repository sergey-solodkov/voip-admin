package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.Option;
import com.github.sergeisolodkov.voipadmin.repository.OptionRepository;
import com.github.sergeisolodkov.voipadmin.service.OptionService;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.OptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.Option}.
 */
@Service
@Transactional
public class OptionServiceImpl implements OptionService {

    private static final Logger LOG = LoggerFactory.getLogger(OptionServiceImpl.class);

    private final OptionRepository optionRepository;

    private final OptionMapper optionMapper;

    public OptionServiceImpl(OptionRepository optionRepository, OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
    }

    @Override
    public OptionDTO save(OptionDTO optionDTO) {
        LOG.debug("Request to save Option : {}", optionDTO);
        Option option = optionMapper.toEntity(optionDTO);
        option = optionRepository.save(option);
        return optionMapper.toDto(option);
    }

    @Override
    public OptionDTO update(OptionDTO optionDTO) {
        LOG.debug("Request to update Option : {}", optionDTO);
        Option option = optionMapper.toEntity(optionDTO);
        option = optionRepository.save(option);
        return optionMapper.toDto(option);
    }

    @Override
    public Optional<OptionDTO> partialUpdate(OptionDTO optionDTO) {
        LOG.debug("Request to partially update Option : {}", optionDTO);

        return optionRepository
            .findById(optionDTO.getId())
            .map(existingOption -> {
                optionMapper.partialUpdate(existingOption, optionDTO);

                return existingOption;
            })
            .map(optionRepository::save)
            .map(optionMapper::toDto);
    }

    public Page<OptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return optionRepository.findAllWithEagerRelationships(pageable).map(optionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OptionDTO> findOne(Long id) {
        LOG.debug("Request to get Option : {}", id);
        return optionRepository.findOneWithEagerRelationships(id).map(optionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Option : {}", id);
        optionRepository.deleteById(id);
    }
}
