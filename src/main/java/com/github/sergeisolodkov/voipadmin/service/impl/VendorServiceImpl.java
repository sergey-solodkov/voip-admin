package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.Vendor;
import com.github.sergeisolodkov.voipadmin.repository.VendorRepository;
import com.github.sergeisolodkov.voipadmin.service.VendorService;
import com.github.sergeisolodkov.voipadmin.service.dto.VendorDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.VendorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.Vendor}.
 */
@Service
@Transactional
public class VendorServiceImpl implements VendorService {

    private static final Logger LOG = LoggerFactory.getLogger(VendorServiceImpl.class);

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public VendorDTO save(VendorDTO vendorDTO) {
        LOG.debug("Request to save Vendor : {}", vendorDTO);
        Vendor vendor = vendorMapper.toEntity(vendorDTO);
        vendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(vendor);
    }

    @Override
    public VendorDTO update(VendorDTO vendorDTO) {
        LOG.debug("Request to update Vendor : {}", vendorDTO);
        Vendor vendor = vendorMapper.toEntity(vendorDTO);
        vendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(vendor);
    }

    @Override
    public Optional<VendorDTO> partialUpdate(VendorDTO vendorDTO) {
        LOG.debug("Request to partially update Vendor : {}", vendorDTO);

        return vendorRepository
            .findById(vendorDTO.getId())
            .map(existingVendor -> {
                vendorMapper.partialUpdate(existingVendor, vendorDTO);

                return existingVendor;
            })
            .map(vendorRepository::save)
            .map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vendors");
        return vendorRepository.findAll(pageable).map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VendorDTO> findOne(Long id) {
        LOG.debug("Request to get Vendor : {}", id);
        return vendorRepository.findById(id).map(vendorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Vendor : {}", id);
        vendorRepository.deleteById(id);
    }
}
