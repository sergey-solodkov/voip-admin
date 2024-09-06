package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.OptionValue;
import com.github.sergeisolodkov.voipadmin.repository.OptionValueRepository;
import com.github.sergeisolodkov.voipadmin.service.OptionValueService;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionValueDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.OptionValueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.OptionValue}.
 */
@Service
@Transactional
public class OptionValueServiceImpl implements OptionValueService {

    private static final Logger LOG = LoggerFactory.getLogger(OptionValueServiceImpl.class);

    private final OptionValueRepository optionValueRepository;

    private final OptionValueMapper optionValueMapper;

    public OptionValueServiceImpl(OptionValueRepository optionValueRepository, OptionValueMapper optionValueMapper) {
        this.optionValueRepository = optionValueRepository;
        this.optionValueMapper = optionValueMapper;
    }

    @Override
    public OptionValueDTO save(OptionValueDTO optionValueDTO) {
        LOG.debug("Request to save OptionValue : {}", optionValueDTO);
        OptionValue optionValue = optionValueMapper.toEntity(optionValueDTO);
        optionValue = optionValueRepository.save(optionValue);
        return optionValueMapper.toDto(optionValue);
    }

    @Override
    public OptionValueDTO update(OptionValueDTO optionValueDTO) {
        LOG.debug("Request to update OptionValue : {}", optionValueDTO);
        OptionValue optionValue = optionValueMapper.toEntity(optionValueDTO);
        optionValue = optionValueRepository.save(optionValue);
        return optionValueMapper.toDto(optionValue);
    }

    @Override
    public Optional<OptionValueDTO> partialUpdate(OptionValueDTO optionValueDTO) {
        LOG.debug("Request to partially update OptionValue : {}", optionValueDTO);

        return optionValueRepository
            .findById(optionValueDTO.getId())
            .map(existingOptionValue -> {
                optionValueMapper.partialUpdate(existingOptionValue, optionValueDTO);

                return existingOptionValue;
            })
            .map(optionValueRepository::save)
            .map(optionValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OptionValueDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OptionValues");
        return optionValueRepository.findAll(pageable).map(optionValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OptionValueDTO> findOne(Long id) {
        LOG.debug("Request to get OptionValue : {}", id);
        return optionValueRepository.findById(id).map(optionValueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OptionValue : {}", id);
        optionValueRepository.deleteById(id);
    }
}
